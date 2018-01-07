import {Component, Input, Output, EventEmitter} from '@angular/core';
import { CurrencyPipe, NgClass } from '@angular/common';
import { Product, InventoryService } from '../services/InventoryService';
import { Account, AccountService } from '../services/AccountService';

@Component({
    selector: 'vending-product',
    styles: [ '.product-button-space { margin: 10px 10px 10px 0px}'],
    template: `
        <div class="col-md-1">
            <input type="button" class="btn-lg product-button-space btn-info"
            [ngClass]="{
                'btn-lg product-button-space': true,
                'btn-info': !emptyProduct,
                'btn-danger': emptyProduct
            }"
            value="{{product.name }} {{product.price | currency:'USD':'symbol'}}"
            (click)="buyProduct($event)">
        </div>
    `
})

export default class ProductComponent {
    private _product: Product;
    emptyProduct = false;

    @Input()
    set product(product: Product) {
        this._product = product;
        this.emptyProduct = this._product.quantity === 0;
    }

    get product() {
        return this._product;
    }

    @Output() message: EventEmitter<string> = new EventEmitter();

    constructor(private inventoryService: InventoryService,
        private accountService: AccountService) {
    }

    buyProduct(event): void {
        const productName = event.currentTarget.value.split(' ')[0];
        const currentBalance = this.accountService.getCurrentLoggedInAccount().currentBalance;
        const product = this.inventoryService.getProductByName(productName);
        let message = '';
        if (product.quantity > 0) {
            if (currentBalance >= product.price) {
                this.inventoryService.reduceQuantityByOne(productName);
                this.accountService.getCurrentLoggedInAccount().currentBalance
                -= this.inventoryService.getPrice(productName);
                message = 'Enjoy your refreshing ' + productName + '!!';
            } else {
                message = 'Insufficient balance to purchase ' + productName +
                ' Please deposit more money';
            }
        } else {
            message = productName + ' is out of stock.';
        }
        this.product = product;
        console.log('Message sent: ' + message);
        this.message.emit(message);
    }

}

