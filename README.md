# Crowdfunding-with-trust-WEB3.0

> WEB3.0 application based on blockchain and powered by React.js.

## Table of contents
* [General info](#general-info)
* [Screenshots](#screenshots)
* [Setup](#setup)
* [Code Examples](#code-examples)
* [Tech/framework used](#techframework-used)
* [Status](#status)
* [Contact](#contact)
* [License](#license)

## General info
>.
>.

## Screenshots

   <details>
       <summary>Main page</summary>
    <ul>
     <img src=""> 
    </ul>
   </details>
	<details>
       <summary>Wallet</summary>
    <ul>
     <img src=""> 
    </ul>
   </details>
	<details>
       <summary>Coin price chart</summary>
    <ul>
     <img src=""> 
    </ul>
   </details>
	<details>
       <summary>Buy/Sell panel</summary>
    <ul>
     <img src=""> 
    </ul>
   </details>

## Setup

>1. npm start dev to start React dev on localhost
>2. npx hardhat
>3. npx hardhat run scripts/deploy.js --network ropsten - to deploy contract to blockchain

> Metamask wallet 
>* Password: **web3.0test**
>* Secret phase: **night, eight, priority, team, album, tooth, seminar, dumb, wedding, churn, save, spice**

## Code Examples
Show examples of usage:


    //SPDX-License-Identifier: UNLICENSED

pragma solidity ^ 0.8.0;

contract Transactions {
    uint256 transactionsCount;

    event Transfer(address sender, address receiver, uint256 amount, 
                    string message, uint256 timestamp, string keyword);

    struct TransferStruct {
        address sender;
        address receiver;
        uint256 amount;
        string message;
        uint256 timestamp;
        string keyword;
    }

    TransferStruct[] transactions;

    function addToBlockchain(address payable receiver, uint amount, string memory message, string memory keyword) public {
        transactionsCount += 1;
        transactions.push(TransferStruct(
            msg.sender,
            receiver,
            amount,
            message,
            block.timestamp,
            keyword
        ));

        emit Transfer(            
            msg.sender,
            receiver,
            amount,
            message,
            block.timestamp,
            keyword
        );
    }

    function getAllTransactions() public view returns (TransferStruct[] memory) {
        return transactions;
    }

    function getTransactionsCount() public view returns (uint256) {
        return transactionsCount;   
    }
}



## Tech/framework used

* Solidity
* React.js
* Tailwind CSS

## Status
Project is in: _development_ phase :monocle_face:

## Contact
[@Bartosz Koscielniak](https://github.com/BartoszKoscielniak)

## License
[MIT](https://choosealicense.com/licenses/mit/) ©

