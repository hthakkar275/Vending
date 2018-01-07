import {Component, Inject} from '@angular/core';
import { NgForm } from '@angular/forms';
import {FormControl, FormGroup} from '@angular/forms';
import { Account, AccountService } from '../services/AccountService';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'vending-withdrawal',
  styleUrls: ['/withdrawal.css'],
  templateUrl: 'withdrawal.html'
})
export default class WithdrawalComponent {
  formModel: FormGroup = new FormGroup({
    'amount': new FormControl(),
    'bankName': new FormControl(),
    'routingNumber': new FormControl(),
    'accountNumber': new FormControl()
  });
  confirmationNumber = 0;
  errorMessage = '';

  constructor(private accountService: AccountService) {
    console.log('Deposit Component constructor');
  }

  onSubmit() {
    console.log('Current balance = ' + this.accountService.getCurrentLoggedInAccount().currentBalance);
    this.accountService.getCurrentLoggedInAccount().currentBalance
     -= this.formModel.value.amount;
     console.log('Transferred ' + this.formModel.value.amount);
     console.log('New balance = ' + this.accountService.getCurrentLoggedInAccount().currentBalance);
     this.confirmationNumber = 100;
  }

  onClear() {
    console.log('Transfer clear');
    this.formModel.reset();
    this.errorMessage = '';
    this.confirmationNumber = 0;
  }
}
