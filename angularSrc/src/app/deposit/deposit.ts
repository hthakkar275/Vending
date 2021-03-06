import {Component, Inject} from '@angular/core';
import { NgForm } from '@angular/forms';
import {FormControl, FormGroup} from '@angular/forms';
import { Account, AccountService } from '../services/AccountService';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'vending-deposit',
  styleUrls: ['/deposit.css'],
  templateUrl: 'deposit.html'
})
export default class DepositComponent {
  formModel: FormGroup = new FormGroup({
    'amount': new FormControl(),
    'ccNumber': new FormControl(),
    'ccExpiration': new FormControl()
  });
  confirmationNumber = 0;
  errorMessage = '';

  constructor(private accountService: AccountService) {
    console.log('Deposit Component constructor');
  }

  onSubmit() {
    console.log('Current balance = ' + this.accountService.getCurrentLoggedInAccount().currentBalance);
    this.accountService.getCurrentLoggedInAccount().currentBalance
     += this.formModel.value.amount;
     console.log('Deposited ' + this.formModel.value.amount);
     console.log('New balance = ' + this.accountService.getCurrentLoggedInAccount().currentBalance);
     this.confirmationNumber = 100;
  }

  onClear() {
    console.log('Deposit clear');
    this.formModel.reset();
    this.errorMessage = '';
    this.confirmationNumber = 0;
  }
}
