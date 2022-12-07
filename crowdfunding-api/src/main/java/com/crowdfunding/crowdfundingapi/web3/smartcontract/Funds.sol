//SPDX-License-Identifier: UNLICENSED

pragma solidity >=0.7.0 <0.9.0;
//pragma solidity ^ 0.8.9;
pragma experimental ABIEncoderV2;

contract Funds {

    address public contractOwner;
    constructor() {
        contractOwner = msg.sender;
    }

    event FundsEmition(address sender, address receiver, uint256 amount, uint256 _phaseId, uint256 timestamp);

    struct FundsStruct {
        address receiver;
        uint256 amount;
        bool isFraud;
        bool isPollEnded;
        uint256 timestamp;
    }

    struct TransactionStruct {
        address sender;
        address receiver;
        uint256 phaseId;
        uint256 amount;
        uint timestamp;
    }

    uint256 public donationsCount;
    mapping (uint256 => FundsStruct) public fundsDonated;
    TransactionStruct[] public transactionHistory;

    function getBalance() public view returns(uint){
        return address(this).balance;
    }

    function depositFunds(address _receiver, uint _amount, uint256 _phaseId) external payable{

        if(fundsDonated[_phaseId].receiver == address(0x0)){
            fundsDonated[_phaseId].receiver = _receiver;
            fundsDonated[_phaseId].amount = _amount;
            fundsDonated[_phaseId].isFraud = false;
            fundsDonated[_phaseId].isPollEnded = false;
            fundsDonated[_phaseId].timestamp = block.timestamp;
        }else{
            fundsDonated[_phaseId].amount += _amount;
        }

        transactionHistory.push(TransactionStruct(
                msg.sender,
                address(this),
                _phaseId,
                _amount,
                block.timestamp
            ));

        emit FundsEmition(
            msg.sender,
            address(this),
            _amount,
            _phaseId,
            block.timestamp
        );

        donationsCount++;
    }

    function getAllDonatedFunds() public view returns (FundsStruct[] memory){
        FundsStruct[] memory temp = new FundsStruct[](donationsCount);
        for (uint i = 0; i < donationsCount; i++){
            temp[i] = fundsDonated[i];
        }

        return temp;
    }

    function getTransactionHiostory() public view returns(TransactionStruct[] memory){
        return transactionHistory;
    }

    function setFraud(uint256 _phaseId) public {
        require(msg.sender == contractOwner, "Only contract owner can set status of poll");
        require(fundsDonated[_phaseId].isPollEnded == false, "Poll already ended");
        fundsDonated[_phaseId].isFraud = true;
    }

    function isFraud(uint256 _phaseId) public view returns(bool){
        return fundsDonated[_phaseId].isFraud;
    }

    function isPollEnded(uint256 _phaseId) public view returns(bool){
        return fundsDonated[_phaseId].isPollEnded;
    }

    function setPollEnded(uint256 _phaseId) public {
        require(msg.sender == contractOwner);
        fundsDonated[_phaseId].isPollEnded = true;
    }

    function sendFundsToOwner(address payable _toReceive, uint256 _phaseId) public {

        require(msg.sender == fundsDonated[_phaseId].receiver,  "Only the collection owner can withdraw funds");
        require(_toReceive == fundsDonated[_phaseId].receiver,  "Only the collection owner can withdraw funds");
        require(fundsDonated[_phaseId].amount > 0,              "Insufficient funds");
        require(fundsDonated[_phaseId].isPollEnded == true,     "Poll has not end");
        require(fundsDonated[_phaseId].isFraud == false,        "Collection status is fraud. You cannot withdraw funds!");

        emit FundsEmition(
            address(this),
            fundsDonated[_phaseId].receiver,
            fundsDonated[_phaseId].amount,
            _phaseId,
            block.timestamp
        );

        transactionHistory.push(TransactionStruct(
                address(this),
                fundsDonated[_phaseId].receiver,
                _phaseId,
                fundsDonated[_phaseId].amount,
                block.timestamp
            ));

        _toReceive.transfer(fundsDonated[_phaseId].amount);
        fundsDonated[_phaseId].amount = 0;
    }

    function sendFundsToDonators(address payable _toReceive, uint256 _phaseId, uint256 _transactionHistoryId) public{
        require(msg.sender == transactionHistory[_transactionHistoryId].sender, "Only the funds owner can withdraw funds");
        require(transactionHistory[_transactionHistoryId].sender == _toReceive, "Only the funds owner can withdraw funds");
        require(transactionHistory[_transactionHistoryId].phaseId == _phaseId,  "Incorrect Phase ID");
        require(fundsDonated[_phaseId].isPollEnded == true,                     "Poll has not end");
        require(fundsDonated[_phaseId].isFraud == true,                         "Collection status is not fraud. You cannot withdraw funds");

        uint256 amountToReturn = transactionHistory[_transactionHistoryId].amount;
        emit FundsEmition(
            address(this),
            _toReceive,
            amountToReturn,
            _phaseId,
            block.timestamp
        );

        _toReceive.transfer(amountToReturn);
        fundsDonated[_phaseId].amount -= amountToReturn;
    }
}