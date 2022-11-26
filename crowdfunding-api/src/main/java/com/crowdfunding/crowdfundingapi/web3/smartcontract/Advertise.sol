//SPDX-License-Identifier: UNLICENSED

pragma solidity >=0.7.0 <0.9.0;
//pragma solidity ^ 0.8.9;
pragma experimental ABIEncoderV2;

contract Advertise {

    event BuyAdvertisementLog(address sender, address receiver, uint256 collectionId, uint256 adType, uint256 promoTo, uint256 timestamp);

    struct AdvertiseStructure {
        address sender;
        address receiver;
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

    function buyAdvertisement(address _receiver, uint256 _collectionId, uint256 _adTypeId) public {

        uint256 _promoTo = block.timestamp + advertiseType[_adTypeId].duration;
        if(advertiseBought[_collectionId].sender != address(0x0)){
            advertiseBought[_collectionId].promoTo = _promoTo;
        }

        if(advertiseBought[_collectionId].sender == address(0x0)){
            advertiseBought[_collectionId].sender = msg.sender;
            advertiseBought[_collectionId].receiver = _receiver;
            advertiseBought[_collectionId].adType = _adTypeId;
            advertiseBought[_collectionId].promoTo = _promoTo;
        }

        emit BuyAdvertisementLog(
            msg.sender,
            _receiver,
            _collectionId,
            _adTypeId,
            _promoTo,
            block.timestamp
        );

    }

    function addAdType(string memory _adName, uint256 _price, uint256 _duration) public {

        availableAdPlansId++;
        advertiseType[availableAdPlansId].adName = _adName;
        advertiseType[availableAdPlansId].price = _price;
        advertiseType[availableAdPlansId].duration = _duration;

    }
}