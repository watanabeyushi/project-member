import {LitElement, html} from 'lit';
import {customElement, property} from 'lit/decorators.js';
import '@google-web-components/google-chart';
import {dataTable} from "@google-web-components/google-chart/loader";

@customElement('google-chart-wrapper')
export class GoogleChartWrapper extends LitElement {

    @property() data = '[["Month", "Days"], ["Jan", 31]]';
    @property() cols: { type: String, label: String }[] = [];
    @property() rows: { c: { v: unknown}[] }[] = [];
    @property() type: String = "area";
    @property() options: undefined = undefined;

    render() {
        return html`
            <google-chart type="${this.type}" .data="${dataTable({cols: this.cols, rows: this.rows})}" options="${this.options}"></google-chart>
        `;
    }

}
