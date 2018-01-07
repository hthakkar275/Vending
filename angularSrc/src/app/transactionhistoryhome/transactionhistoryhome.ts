import {Component} from '@angular/core';
import { Transaction, DepositTransaction, WithdrawalTransaction,
    PurchaseTransaction, TransactionHistoryService } from '../services/TransactionHistoryService';
import { Account, AccountService } from '../services/AccountService';

@Component({
    selector: 'vending-transaction-history-home',
    template: `<br><br><br>
    <div>
        <vending-transaction-history [transactions]="transactionsHistory">
        </vending-transaction-history>
    </div>`
})

export default class TransactionHistoryHomeComponent  {

    transactionsHistory = '';

    constructor(private transactionHistoryService: TransactionHistoryService,
        private accountService: AccountService) {
        console.log('Transaction History Component constructor');
        const currentLoggedInUser = this.accountService.currentLoggedInAccount;
        const transactions = this.transactionHistoryService.getTransactions(currentLoggedInUser.accountId);
        for (const transaction of transactions) {
            let transactionStr = '';
            transactionStr += transaction.time.toLocaleDateString() + ' ';
            transactionStr += transaction.time.toLocaleTimeString() + '   ';
            transactionStr += transaction.transactionType + '   ';
            if (transaction.transactionType === 'DEPOSIT') {
                transactionStr += '         ';
                const depositTransaction = <DepositTransaction> transaction;
                transactionStr += depositTransaction.amount.toFixed(2) + '  ';
            } else if (transaction.transactionType === 'PURCHASE') {
                transactionStr += '     ';
                const purchaseTransaction = <PurchaseTransaction> transaction;
                transactionStr += purchaseTransaction.amount.toFixed(2)  + '  ';
                transactionStr += purchaseTransaction.productName + ' ';
            } else if (transaction.transactionType === 'WITHDRAWAL') {
                const withdrawalTransaction = <WithdrawalTransaction> transaction;
                transactionStr += withdrawalTransaction.amount.toFixed(2) + ' ';
                transactionStr += withdrawalTransaction.bankName;
            }
            transactionStr += '\r';
            this.transactionsHistory += transactionStr;
        }
    }

}

