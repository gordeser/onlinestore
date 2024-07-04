# Online grocery store

## Description

The project is online store where users can order products and leave comments on products' pages.

## Conceptual model

![diagram](asserts/diagram.png)

## Business operation

- **Order placement**: Users can place orders and track their status. User can place an order only if the sum of goods is not less than 300 CZK and not more than 100,000 CZK

## Complex query

- **Select products with a certain category**: `SELECT id_product FROM Product WHERE category='some_category'`

## Client-side application

[onlinestore-client](https://github.com/gordeser/onlinestore-client)
