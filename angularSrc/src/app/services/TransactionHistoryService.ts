import { Injectable } from '@angular/core';

export class Transaction {
    constructor(
        public time: Date,
        public accountId: number,
        public transactionType: string,
        public amount: number
    ) {}
}

export class DepositTransaction extends Transaction {
    constructor(
        public time: Date,
        public accountId: number,
        public amount: number,
        public confirmationNumber: number
    ) { super(time, accountId, 'DEPOSIT', amount); }
}

export class WithdrawalTransaction extends Transaction {
    constructor(
        public time: Date,
        public accountId: number,
        public amount: number,
        public confirmationNumber: number,
        public bankName: string
    ) { super(time, accountId, 'WITHDRAWAL', amount); }
}

export class PurchaseTransaction extends Transaction {
    constructor(
        public time: Date,
        public accountId: number,
        public amount: number,
        public productName: string
    ) { super(time, accountId, 'PURCHASE', amount); }
}

@Injectable()
export class TransactionHistoryService {
    transactions:  Array<Transaction> = new Array<Transaction>();
    currentLoggedInAccount: Account;

    constructor() {
        console.log('IN TRANSACTION HISTORY CONSTRUCTOR');

        this.setTransaction(new DepositTransaction(new Date(), 1001, 30.00, 100));
        this.setTransaction(new PurchaseTransaction(new Date(), 1001, 2.25, 'Pepsi'));
        this.setTransaction(new PurchaseTransaction(new Date(), 1001, 2.25, 'Coke'));
        this.setTransaction(new PurchaseTransaction(new Date(), 1001, 2.75, 'Aquafina Water'));
        this.setTransaction(new PurchaseTransaction(new Date(), 1001, 2.50, 'Gatorade'));
        this.setTransaction(new PurchaseTransaction(new Date(), 1001, 2.25, 'Pepsi'));
        this.setTransaction(new PurchaseTransaction(new Date(), 1001, 2.25, 'Pepsi'));
        this.setTransaction(new WithdrawalTransaction(new Date(), 1001, 5.50, 101, 'JPM'));

        this.setTransaction(new DepositTransaction(new Date(), 1003, 50.00, 100));
        this.setTransaction(new PurchaseTransaction(new Date(), 1003, 1.25, 'Pepsi'));
        this.setTransaction(new PurchaseTransaction(new Date(), 1003, 1.25, 'Coke'));
        this.setTransaction(new PurchaseTransaction(new Date(), 1003, 1.75, 'Aquafina Water'));
        this.setTransaction(new PurchaseTransaction(new Date(), 1003, 1.50, 'Gatorade'));
        this.setTransaction(new PurchaseTransaction(new Date(), 1003, 1.25, 'Pepsi'));
        this.setTransaction(new PurchaseTransaction(new Date(), 1003, 1.25, 'Pepsi'));
        this.setTransaction(new WithdrawalTransaction(new Date(), 1003, 10.50, 101, 'JPM'));

    }

    getTransactions(accountId: number): Array<Transaction> {
        return this.transactions.filter(t => t.accountId === accountId);
    }

    setTransaction(transaction: Transaction): void {
        this.transactions.push(transaction);
    }
}



