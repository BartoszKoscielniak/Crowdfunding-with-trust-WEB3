//SPDX-License-Identifier: UNLICENSED

pragma solidity >=0.7.0 <0.9.0;
//pragma solidity ^ 0.8.9;
pragma experimental ABIEncoderV2;

contract Advertise {

    address public contractOwner;

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

    event BuyAdvertisementLog(address sender, uint256 collectionId, uint256 adType, uint256 promoTo, uint256 timestamp);

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

    function buyAdvertisement(uint256 _collectionId, uint256 _adTypeId) public payable{
        require(msg.value == advertiseType[_adTypeId].price,    "Insufficient funds");
        require(advertiseType[_adTypeId].duration != 0,         "Type does not exist");

        uint256 _promoTo = advertiseType[_adTypeId].duration;
        if(advertiseBought[_collectionId].sender != address(0x0)){
            advertiseBought[_collectionId].promoTo += _promoTo;
        } else {
            advertiseBought[_collectionId].sender = msg.sender;
            advertiseBought[_collectionId].adType = _adTypeId;
            advertiseBought[_collectionId].promoTo = block.timestamp + _promoTo;
        }

        emit BuyAdvertisementLog(
            msg.sender,
            _collectionId,
            _adTypeId,
            _promoTo,
            block.timestamp
        );
    }

    function addAdType(string memory _adName, uint256 _price, uint256 _duration) public {
        require(contractOwner == msg.sender,                    "Only the contract owner can add advertise types");
        advertiseType[availableAdPlansId].adName = _adName;
        advertiseType[availableAdPlansId].price = _price;
        advertiseType[availableAdPlansId].duration = _duration;
        availableAdPlansId++;
    }
}