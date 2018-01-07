import {Component, Input} from '@angular/core';
import { OnInit, OnDestroy } from '@angular/core';
import { Product } from '../services/InventoryService';

@Component({
  selector: 'vending-temp-message',
  styles: [ '.empty-message-margin { margin: 9px}; .nonempty-message-margin { margin: 100px};'],
  template: `
    <div class="nonempty-message-margin" *ngIf="show; else elseBlock"><h4>{{_message}}</h4></div>
    <ng-template #elseBlock><div class="empty-message-margin"><br></div></ng-template>
  `
})

export class TemporaryMessageComponent implements OnInit, OnDestroy {

    private _message: string;
    show = false;

    @Input()
    set message(message: string) {
        console.log('In message setter');
        this.show = message.length > 0;
        console.log('In message setter: show = ' + this.show);
        this._message = message;
    }

    get message() {
        return this._message;
    }

    ngOnInit() {
        console.log('temporary-message: ngOnInit');
    }

    ngOnDestroy() {
        console.log('temporary-message: ngOnDestroy');
    }
}
