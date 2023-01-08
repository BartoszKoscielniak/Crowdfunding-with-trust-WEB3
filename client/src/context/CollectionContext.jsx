import React, {useEffect, useState} from "react";
import { ethers } from 'ethers';

export const CollectionContext = React.createContext();

const { ethereum } = window;

const sessionStorageState = (key, receivedValue) => {
    const [value, setValue] = useState(JSON.parse(localStorage.getItem(key)) ?? receivedValue);

    useEffect(() => {
      localStorage.setItem(key, JSON.stringify(value));
    }, [value, key]);
    
    return [value, setValue];
}

export const CollectionProvider = ({children}) => {
    
    const URL = 'http://localhost:8080';
    const [login, setLogin]                             = sessionStorageState('login', "");
    const [registerData, setRegisterData]               = useState({nameInput: '', surnameInput: '', emailInput: '', phoneInput: '', keyInput: '', passwordInput: ''})
    const [authenticated, setAuthenticated]             = sessionStorageState('authenticated', false);
    const [collections, setCollections]                 = useState(null)
    const [collectionError, setCollectionError]         = useState(null)
    const [collectionSuccess, setCollectionSuccess]     = useState(null)

    const handleChangeRegister = (e, name) => {
        setRegisterData((prevState) => ({...prevState, [name]: e.target.value}));
    }

    const GetAllCollections = async (searchQuery, type) => {
        let apiCall = '/api/collection'
        if(searchQuery !== undefined && type === undefined) {
            apiCall += '?name=' + searchQuery;
        }

        if(type !== undefined && searchQuery === undefined) {
            apiCall += '?type=' + type.toUpperCase();
        }

        if(searchQuery !== undefined && type !== undefined) {
            apiCall += '?type=' + type.toUpperCase() + '&name=' + searchQuery;
        }

        return await fetch(`${URL}${apiCall}`, {
            method: 'GET',
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Access-Control-Allow-Origin': "*"
            },
        })
        .then(response => response.json())
        .then(data => {
            setCollections(data);
        })
        .catch(err => {
            setCollectionError("Failed to fetch collections. Try again later!")
        });
    }

    const DepositFunds = async (phaseId, amount, authToken) => {

        return await fetch(`${URL}/api/web3/fund/depositfunds?phaseId=${phaseId}&amount=${amount}`, {
            method: 'POST',
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Access-Control-Allow-Origin': "*",
                'Authorization' : authToken
            },
        })
        .then(response => response.json())
        .then(data => {
            if(data['error'] !== undefined){
                setCollectionError(data['error'])//result
            }else{
                setCollectionSuccess(<div><p className='inline-block'>Success! Check Your transaction here: </p> <a className='underline-offset-1 inline-block underline' href={`https://goerli.etherscan.io/tx/${data['result']}`} target='_blank' rel='noopener noreferrer'>etherscan.io</a></div>)
                GetAllCollections()
            }
        })
        .catch(err => {
            setCollectionError(err.message)
        });
    }

    useEffect(() => {

    }, []);

    return (
        <CollectionContext.Provider value={{ 
            GetAllCollections,
            collections,
            collectionError,
            collectionSuccess,
            setCollectionError,
            setCollectionSuccess,
            DepositFunds
            }}
            >
            {children}
        </CollectionContext.Provider>
    );
};