import { Injectable } from '@angular/core';
import {HttpModule, Http} from '@angular/http';

export class Account {
    constructor(
        public accountId: number,
        public firstName: string,
        public lastName: string,
        public currentBalance: number
    ) {}
}

@Injectable()
export class AccountService {
    accounts:  Array<Account>;
    currentLoggedInAccount: Account;

    constructor() {
        console.log('IN ACCOUNT SERVICE CONSTRUCTOR');
        this.accounts = initialAccounts.map(a => new Account(a.accountId,
            a.firstName, a.lastName, a.currentBalance));
        this.currentLoggedInAccount = this.accounts.pop();
    }

    getCurrentLoggedInAccount(): Account {
        return this.currentLoggedInAccount;
    }

    getCurrentBalance(): number {
        return this.currentLoggedInAccount.currentBalance;
    }
}

const initialAccounts = [
    {
        'accountId' : 1001,
        'firstName' : 'Joe',
        'lastName' : 'Smith',
        'currentBalance' : 32.00
    },
    {
        'accountId' : 1002,
        'firstName' : 'John',
        'lastName' : 'Doe',
        'currentBalance' : 22.00
    },
    {
        'accountId' : 1003,
        'firstName' : 'Jane',
        'lastName' : 'Doe',
        'currentBalance' : 5.00
    },
];
