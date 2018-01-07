import {Component, Inject} from '@angular/core';
import { OnInit, OnChanges } from '@angular/core';
import { InventoryService } from '../services/InventoryService';
import { Account, AccountService } from '../services/AccountService';
import { CurrencyPipe } from '@angular/common';
import ProductComponent from '../product/product';
import {TimerObservable} from 'rxjs/observable/TimerObservable';
import {Subscription} from 'rxjs/Subscription';

@Component({
    selector: 'vending-home-page',
    styleUrls: ['/home.css'],
    templateUrl: './home.html'
})

export default class HomeComponent implements OnInit, OnChanges {

    products = [];
    account: Account;
    message = '';

    constructor(private inventoryService: InventoryService,
        private accountService: AccountService) {
        console.log('Home Component constructor');
    }

    ngOnInit() {
      console.log('In HomeComponent ngOnInit');
      this.inventoryService.getProducts().forEach(name => {
        this.products.push({
            'name' : name,
            'price' : this.inventoryService.getPrice(name),
            'quantity' : this.inventoryService.getQuantity(name)
        });
      });
      this.account = this.accountService.getCurrentLoggedInAccount();
      console.log('Account: ' + this.account);
    }

    ngOnChanges() {
        console.log('Inside home ngOnChanges');
    }
    handleMessage(message: string) {
        console.log('Message before receipt: ' + this.message);
        this.message = message;
        console.log('Message after receipt: ' + this.message);
        const timer = TimerObservable.create(4000);
        const subscription = timer.subscribe(t => {
            console.log('Temporary-message: wakeup: ' + this.message);
            subscription.unsubscribe();
            this.message = '';
        });

    }
}

