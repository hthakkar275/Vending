import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import NavbarComponent from './navbar/navbar';
import HomeComponent from './home/home';
import ProductComponent from './product/product';
import {TemporaryMessageComponent} from './temporary-message/temporary-message';
import {InventoryService} from './services/InventoryService';
import {AccountService} from './services/AccountService';
import DepositHomeComponent from './deposithome/deposithome';
import DepositComponent from './deposit/deposit';
import WithdrawalHomeComponent from './withdrawalhome/withdrawalhome';
import WithdrawalComponent from './withdrawal/withdrawal';
import TransactionHistoryHomeComponent from './transactionhistoryhome/transactionhistoryhome';
import TransactionHistoryComponent from './transactionhistory/transactionhistory';
import {TransactionHistoryService} from './services/TransactionHistoryService';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    ProductComponent,
    TemporaryMessageComponent,
    DepositHomeComponent,
    DepositComponent,
    WithdrawalHomeComponent,
    WithdrawalComponent,
    TransactionHistoryHomeComponent,
    TransactionHistoryComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule.forRoot([
      {path: '', component: HomeComponent},
      {path: 'deposit', component: DepositHomeComponent},
      {path: 'withdraw', component: WithdrawalHomeComponent},
      {path: 'transactions', component: TransactionHistoryHomeComponent}
    ])
  ],
  providers: [{provide: AccountService, useClass: AccountService},
    {provide: InventoryService, useClass: InventoryService},
    {provide: TransactionHistoryService, useClass: TransactionHistoryService}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
