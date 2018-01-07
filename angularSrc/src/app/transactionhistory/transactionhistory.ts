import {Component, Input} from '@angular/core';
import { NgForm } from '@angular/forms';
import {FormControl, FormGroup} from '@angular/forms';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'vending-transaction-history',
  styleUrls: ['/transactionhistory.css'],
  templateUrl: 'transactionhistory.html'
})
export default class TransactionHistoryComponent {

    @Input() transactions: string;

}
