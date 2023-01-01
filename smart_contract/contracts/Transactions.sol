//SPDX-License-Identifier: UNLICENSED

pragma solidity ^ 0.8.0;

contract Transactions {
    uint256 transactionsCount;

    event Transfer(address sender, address receiver, uint256 amount, 
                    string message, uint256 timestamp, string keyword);

    struct TransferStruct {//konstruktor
        address sender;
        address receiver;
        uint256 amount;
        string message;
        uint256 timestamp;
        string keyword;
    }

    TransferStruct[] transactions;//tablica z transakcjami transakcji

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