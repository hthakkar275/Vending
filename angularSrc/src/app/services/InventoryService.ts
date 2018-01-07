import { Injectable } from '@angular/core';

export class Product {
    constructor(
        public name: string,
        public price: number,
        public quantity: number
    ) {}
}

@Injectable()
export class InventoryService {

    products: Array<Product>;
    MAX_QUANTITY_PER_PRODUCT = 50;

    constructor() {
        console.log('IN INVENTORY SERVICE CONSTRUCTOR');
        this.products = initialProductInventory.map(p => new Product(p.name, p.price, p.quantity));
    }

    getProducts(): Array<string> {
        return this.products.map(p => p.name);
    }

    getPrice(name: string): number {
        return this.products.find(p => p.name === name).price;
    }

    getQuantity(name: string): number {
        return this.products.find(p => p.name === name).quantity;
    }

    getProductByName(name: string): Product {
        return this.products.find(p => p.name === name);
    }

    deconfigureProduct(name: string) {
        this.products = this.products.filter(p => p.name !== name);
    }

    reduceQuantityByOne(name: string) {
        this.products.find(p => p.name === name).quantity--;
    }

    reduceQuantity(name: string, reduceBy: number) {
        this.products.find(p => p.name === name).quantity -= reduceBy;
    }

    reduceAllQuantity(name: string) {
        this.products.find(p => p.name === name).quantity = 0;
    }

    addQuantity(name: string, addBy: number) {
        const currentQuantity: number = this.products.find(p => p.name === name).quantity;
        if (currentQuantity + addBy > this.MAX_QUANTITY_PER_PRODUCT) {
            throw new Error('Can not put more than maximum ' + this.MAX_QUANTITY_PER_PRODUCT);
        }
        this.products.find(p => p.name === name).quantity += addBy;
    }

    changePrice(name: string, newPrice: number) {
        this.products.find(p => p.name === name).price = newPrice;
    }
}

const initialProductInventory = [
    {
        'name' : 'Coke',
        'price' : 1.25,
        'quantity' : 0
    },
    {
        'name' : 'Pepsi',
        'price' : 1.25,
        'quantity' : 20
    },
    {
        'name' : 'Aquafina-Water',
        'price' : 1.50,
        'quantity' : 15
    },
    {
        'name' : 'Gatorade',
        'price' : 1.75,
        'quantity' : 25
    }
];
