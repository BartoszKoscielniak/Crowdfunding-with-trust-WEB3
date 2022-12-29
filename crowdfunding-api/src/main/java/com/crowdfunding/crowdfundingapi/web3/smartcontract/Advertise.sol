//SPDX-License-Identifier: UNLICENSED

pragma solidity >=0.7.0 <0.9.0;
//pragma solidity ^ 0.8.9;
pragma experimental ABIEncoderV2;

contract Advertise {

    address public contractOwner;

    event AdvertiseLog(address sender, address receiver, uint256 _collectionId, uint256 amount, uint256 _adType, uint256 _promoTo,  uint256 _timestamp);

    struct TransactionStruct {
        address sender;
        address receiver;
        uint256 _collectionId;
        uint256 amount;
        uint256 _adType;
        uint256 _promoTo;
        uint timestamp;
    }

    struct AdvertiseStructure {
        address sender;
        uint256 promoTo;
        uint256 adType;
    }

    struct AdvertiseTypes {
        string adName;
        uint256 price;
        uint256 duration;
    }

    uint256 public availableAdPlansId ;
    mapping (uint256 => AdvertiseStructure) public advertiseBought;
    mapping (uint256 => AdvertiseTypes) public advertiseType;
    TransactionStruct[] public transactionHistory;


    constructor() {
        contractOwner = msg.sender;
    }

    function getBalance() public view returns(uint){
        return address(this).balance;
    }

    function withdrawAll(address payable _to) public {
        require(contractOwner == msg.sender,                "Only the contract owner can withdraw funds");
        require(contractOwner == _to,                       "Only the contract owner can withdraw funds");
        _to.transfer(address(this).balance);
    }

    function buyAdvertisement(uint256 _collectionId, uint256 _adTypeId) public payable{
        require(advertiseType[_adTypeId].duration != 0,         "Type does not exist");
        require(msg.value == advertiseType[_adTypeId].price,    "Insufficient funds");

        if(advertiseBought[_collectionId].sender != address(0x0)){
            advertiseBought[_collectionId].sender = msg.sender;
            advertiseBought[_collectionId].adType = _adTypeId;
            advertiseBought[_collectionId].promoTo += advertiseType[_adTypeId].duration;
        } else {
            advertiseBought[_collectionId].sender = msg.sender;
            advertiseBought[_collectionId].adType = _adTypeId;
            advertiseBought[_collectionId].promoTo = block.timestamp + advertiseType[_adTypeId].duration;
        }

        transactionHistory.push(TransactionStruct(
                msg.sender,
                address(this),
                _collectionId,
                advertiseType[_adTypeId].price,
                _adTypeId,
                advertiseBought[_collectionId].promoTo,
                block.timestamp
            ));

        emit AdvertiseLog(
            msg.sender,
            address(this),
            _collectionId,
            advertiseType[_adTypeId].price,
            _adTypeId,
            advertiseBought[_collectionId].promoTo,
            block.timestamp
        );
    }

    function getTransactionHiostory() public view returns(TransactionStruct[] memory){
        return transactionHistory;
    }

    function addAdType(string memory _adName, uint256 _price, uint256 _duration) public {
        require(contractOwner == msg.sender,                    "Only the contract owner can add advertise types");
        advertiseType[availableAdPlansId].adName = _adName;
        advertiseType[availableAdPlansId].price = _price;
        advertiseType[availableAdPlansId].duration = _duration;
        availableAdPlansId++;
    }
}