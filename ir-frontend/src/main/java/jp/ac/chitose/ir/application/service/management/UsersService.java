package jp.ac.chitose.ir.application.service.management;

import jp.ac.chitose.ir.application.exception.UserManagementException;
import jp.ac.chitose.ir.infrastructure.repository.AuthenticationRepository;
import jp.ac.chitose.ir.infrastructure.repository.UsersRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class)
public class UsersService {
    private final UsersRepository usersRepository;
    private final AuthenticationRepository authenticationRepository;
    private final RoleService roleService;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, AuthenticationRepository authenticationRepository, RoleService roleService, SecurityService securityService, PasswordEncoder passwordEncoder){
        this.usersRepository = usersRepository;
        this.authenticationRepository = authenticationRepository;
        this.roleService = roleService;
        this.securityService = securityService;
        this.passwordEncoder = passwordEncoder;
    }

    // ユーザ追加(単体)
    public void addUser(String loginId, String username, String password, Set<Integer> selectedRoleIds) throws UserManagementException{
        // 入力情報が空の場所があった場合1を返す
        if(StringUtils.isEmpty(loginId) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || selectedRoleIds.isEmpty())
            throw new UserManagementException("空の入力欄があります");

        // loginidでユーザが取得できるか判断
        // 既に登録されてたら2を返す
        if(usersRepository.getUsersCount(loginId) > 0)
            throw new UserManagementException("ログインIDが既に存在しています");

        // パスワードの書式が正しいか判定
        if(!this.checkPasswordFormat(password))
            throw new UserManagementException("パスワードの要件を満たしていません");

        // パスワードのハッシュ化
        String encodedPassword = passwordEncoder.encode(password);

        // usersテーブルにユーザを追加
        long userId = usersRepository.addUser(loginId, username, encodedPassword);

        // user_roleテーブルに追加
        this.addRolesFromCheckBox(userId, selectedRoleIds);
    }

    // csvによるユーザ一括追加
    // 途中でおかしくなったらロールバックしてデータが残らないようにする
    public long addUsers(InputStream inputStream) throws UserManagementException{
        //csvの読み込み
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            // csvファイルが空か判定するための変数
            boolean isEmpty = true;
            // 行数を格納する変数
            long rowNumber = 0;

            while ((line = reader.readLine()) != null) {
                rowNumber++;
                isEmpty = false;
                String[] data = line.split(",");

                // データが不足している場合
                if(data.length < 4) {
                    throw new UserManagementException(rowNumber + " 行目のデータが不足しています");
                }
                // csvのユーザ情報を取得
                // BOM(65279)が悪さをするので消してから代入
                // todo 正規表現の導入の検討(期間内にできる？後継に投げる？一旦考えない？)
                String loginId = data[0].replace(String.valueOf((char)65279), "");
                String username = data[1].replace(String.valueOf((char)65279), "");
                String password = data[2].replace(String.valueOf((char)65279), "");

                // 既にログインIDが登録されているか判定→登録されていればエラーを返す
                if(usersRepository.getUsersCount(loginId) > 0)
                    throw new UserManagementException(rowNumber + " 行目のユーザのログインID " + loginId + " は既に存在します");

                // パスワードの書式が正しいか判定
                if(!this.checkPasswordFormat(password))
                    throw new UserManagementException(rowNumber + " 行目のユーザのパスワードは要件を満たしていません");

                // パスワードのハッシュ化
                String encodedPassword = passwordEncoder.encode(password);

                try {
                    // ユーザを追加する
                    long userId = usersRepository.addUser(loginId, username, encodedPassword);

                    // ロールを追加する
                    for(int i=3;i<data.length;i++){
                        Optional<Integer> roleIdOp = roleService.getRoleId(data[i]);
                        if(roleIdOp.isEmpty()) throw new UserManagementException(rowNumber + " 行目: " + data[i] + " というロールは存在しません");

                        usersRepository.addRole(userId, roleIdOp.get());
                    }
                } catch (UserManagementException e){
                    throw e;
                } catch (DuplicateKeyException e){
                    e.printStackTrace();
                    throw new UserManagementException(rowNumber + " 行目のユーザのログインIDは既に存在します");
                } catch (RuntimeException e){
                    e.printStackTrace();
                    throw new UserManagementException(rowNumber + " 行目の処理中にエラーが発生しました");
                }
            }
            // csvが空の場合エラーを返す
            if(isEmpty) throw new UserManagementException("csvファイルが空です");

            // 正常終了する場合追加したユーザ数を返す
            return rowNumber;
        } catch (IOException e) {
            e.printStackTrace();
            throw new UserManagementException("csvファイルが正しく読み込めませんでした");
        }
    }

    // ユーザ更新
    public UsersData updateUser(User targetUser, String loginId, String username, String password, Set<Integer> selectedRoleIds) throws UserManagementException{
        // ユーザ・ロールともに全て空欄だった場合エラーを返す
        if(StringUtils.isEmpty(loginId) && StringUtils.isEmpty(username) && StringUtils.isEmpty(password) && selectedRoleIds.isEmpty())
            throw new UserManagementException("変更内容を入力してください");

        // 変更の有無を判定する変数
        boolean existUpdateData = false;

        // 各項目の値が空ならば現在のデータを設定する
        // 空でない項目があれば existUpdateData を true に変更
        // todo 正規表現などを利用するのであればelse文に処理を追加
        if(StringUtils.isEmpty(loginId)) loginId = targetUser.login_id();
        else {
            // 変更があった場合既にログインIDが登録されているか判定→登録されていればエラーを返す
            if(usersRepository.getUsersCount(loginId) > 0) throw new UserManagementException("ログインID " + loginId + " は既に存在します");
            existUpdateData = true;
        }

        if(StringUtils.isEmpty(username)) username = targetUser.name();
        else existUpdateData = true;

        if(StringUtils.isEmpty(password)) password = authenticationRepository.getPassword(securityService.getLoginUser().getAccountId()).get().value();
        else{
            // パスワードの書式が正しいか判定
            if(!this.checkPasswordFormat(password))
                throw new UserManagementException("パスワードの要件を満たしていません");

            // パスワードのハッシュ化
            password = passwordEncoder.encode(password);
            existUpdateData = true;
        }

        // ユーザ情報を変更
        if(existUpdateData) usersRepository.updateUser(targetUser.id(), loginId, username, password);

        // ロールの変更がある場合ロールを全て消してから追加する
        if(!selectedRoleIds.isEmpty()){
            usersRepository.deleteRoles(targetUser.id());
            this.addRolesFromCheckBox(targetUser.id(), selectedRoleIds);
        }

        return usersRepository.getUsersData(targetUser.id()).get();
    }

    // ユーザ無効化
    // 途中でおかしくなったら例外を投げてロールバック
    public void deactivateUsers(Set<UsersData> selectedUsers) throws UserManagementException{
        // 1件ずつユーザー情報を取り出して操作する
        for (UsersData user : selectedUsers) {
            long id = user.id();
            if(id == securityService.getLoginUser().getAccountId()) {
                throw new UserManagementException("現在ログイン中のユーザが含まれています");
            }

            int deleted = usersRepository.deactivateUser(id);

            if(deleted == 0) throw new UserManagementException(user.user_name() + "の削除に失敗");
        }
    }

    // ユーザ有効化
    public void activateUsers(Set<UsersData> selectedUsers) throws UserManagementException{
        // 1件ずつユーザー情報を取り出して操作する
        for (UsersData user : selectedUsers) {
            long id = user.id();
            int revived = usersRepository.activateUser(id);

            if(revived == 0) throw new UserManagementException(user.user_name() + "の削除に失敗");
        }
    }

    public void updateLoginUserPassword(String prePassword, String newPassword, String confirmPassword) throws UserManagementException{
        // 入力情報が空の場所があった場合エラーを返す
        if(StringUtils.isEmpty(prePassword) || StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(confirmPassword))
            throw new UserManagementException("空の入力欄が存在します");

        // 現在のパスワードをuser_idから取得
        String encodedDBPassword = authenticationRepository.getPassword(securityService.getLoginUser().getAccountId()).get().value();

        // 現在のパスワードの入力が異なっている場合エラーを返す
        if(!passwordEncoder.matches(prePassword, encodedDBPassword))
            throw new UserManagementException("現在のパスワードが正しくありません");

        // 新しいパスワードが異なっている場合エラーを返す
        if(!newPassword.equals(confirmPassword))
            throw new UserManagementException("新しいパスワードが一致していません");

        // 新しいパスワードと現在のパスワードが一致していた場合エラーを返す
        if(newPassword.equals(prePassword))
            throw new UserManagementException("現在のパスワードと新しいパスワードが一致しています");

        // パスワードの書式が正しいか判定
        if(!this.checkPasswordFormat(newPassword))
            throw new UserManagementException("新しいパスワードは要件を満たしていません");

        // 正常な場合パスワードを変更する
        String encodedPassword = passwordEncoder.encode(newPassword);
        usersRepository.updatePassword(securityService.getLoginUser().getAccountId(), encodedPassword);
    }

    // チェックボックスを用いたロール追加
    private void addRolesFromCheckBox(long userId, Set<Integer> selectedRoleIds){
        for(int roleId : selectedRoleIds){
            usersRepository.addRole(userId, roleId);
        }
    }

    // パスワードの要件を満たしているか判定
    // 現状は12文字以上だけだが、他に条件が追加される可能性もあるのでメソッド化
    // 条件が追加されて表示分けの必要などがあれば戻り値を適切に変更
    private boolean checkPasswordFormat(String password){
        return password.length() >= 12;
    }

}
