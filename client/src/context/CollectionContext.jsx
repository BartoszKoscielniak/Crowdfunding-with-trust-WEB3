import React, { useEffect, useState, useContext } from "react";
import { AccessContext } from "./AccessContext";

export const CollectionContext = React.createContext();

export const CollectionProvider = ({ children }) => {

    const URL = 'http://localhost:8080';
    const [registerData, setRegisterData]                   = useState({ nameInput: '', surnameInput: '', emailInput: '', phoneInput: '', keyInput: '', passwordInput: '' })
    const [collections, setCollections]                     = useState(null)
    const [collectionError, setCollectionErrorState]             = useState(null)
    const [collectionSuccess, setCollectionSuccessState]         = useState(null)
    const [openedCollectionModal, setOpenedCollectionModal] = useState(null)
    const [supportedFraudPhases, setSupportedFraudPhases]   = useState(null);
    const [ownedSuccessPhases, setOwnedSuccessPhases]       = useState(null)
    const { setAuthenticated, setAccessError }              = useContext(AccessContext);

    const setCollectionError = (msg) => {
        setCollectionSuccessState(null)
        setCollectionErrorState(msg)
    }

    const setCollectionSuccess = (msg) => {
        setCollectionSuccessState(msg)
        setCollectionErrorState(null)
    }

    const GetAllCollections = async (authToken, searchQuery, type) => {
        let apiCall = '/api/collection?excludeRequester=true'
        if (searchQuery !== undefined && type === undefined) {
            apiCall += '&name=' + searchQuery;
        }

        if (type !== undefined && searchQuery === undefined) {
            apiCall += '&type=' + type.toUpperCase();
        }

        if (searchQuery !== undefined && type !== undefined) {
            apiCall += '&type=' + type.toUpperCase() + '&name=' + searchQuery;
        }

        performRequest(
            authToken,
            `${apiCall}`,
            "GET"
        )
        .then(data => {
            if(data !== undefined){
                setCollections(data);
            }
        })
    }

    const DepositFunds = async (phaseId, amount, authToken) => {
        performRequest(
            authToken,
            `/api/web3/fund/depositfunds?phaseId=${phaseId}&amount=${amount}`,
            "POST"
        )
        .then(data => {
            if(data !== undefined){
                if (data['error'] !== undefined) {
                        if(data['error'] === 'replacement transaction underpriced' || data['error'] === 'nonce too low'){
                            setCollectionError("Please wait until previous transaction is processed")
                        }else {
                            setCollectionError(data['error'])
                        }
                } else {
                    setCollectionSuccess(<div><p className='inline-block'>Success! Check Your transaction here: </p> <a className='underline-offset-1 inline-block underline' href={`https://goerli.etherscan.io/tx/${data['result']}`} target='_blank' rel='noopener noreferrer'>etherscan.io</a></div>)
                    GetAllCollections(authToken)
                }
            }
        })
    }

    const PublishCollection = async (authToken, collectionId) => {
        performRequest(
            authToken,
            `/api/collection/publish?collectionId=${collectionId}`,
            "POST"
        )
        .then(data => {
            if(data !== undefined){
                if (data['error'] !== undefined) {
                    setCollectionError(data['error'])
                } else {
                    setCollectionSuccess(data['result'])
                    GetUsersCollections(authToken)
                }
            }
        })
    }

    const DeleteCollection = async (authToken, collectionId) => {
        performRequest(
            authToken,
            `/api/collection/${collectionId}`,
            "DELETE"
        )
        .then(data => {
            if(data !== undefined){
                if (data['error'] !== undefined) {
                    setCollectionError(data['error'])
                } else {
                    setCollectionSuccess(data['result'])
                    window.location.reload();
                }
            }
        })
    }

    const AddCollection = async (collectionData, type, authToken) => {
        const { collectionNameInput, collectionDescriptionInput,
            phase1NameInput, phase1DescriptionInput, phase1GoalInput, phase1TillInput, phase1PoeInput,
            phase2NameInput, phase2DescriptionInput, phase2GoalInput, phase2TillInput, phase2PoeInput,
            phase3NameInput, phase3DescriptionInput, phase3GoalInput, phase3TillInput, phase3PoeInput,
            phase4NameInput, phase4DescriptionInput, phase4GoalInput, phase4TillInput, phase4PoeInput } = collectionData

        if (collectionNameInput.length === 0 || collectionDescriptionInput.length === 0 || phase1NameInput.length === 0 ||
            phase1DescriptionInput.length === 0 || phase1GoalInput.length === 0 || phase1TillInput.length === 0) {
            setCollectionError("Fill in all required fileds!")
        } else {               
            performRequest(
                authToken,
                `/api/collection?description=${collectionDescriptionInput}&name=${collectionNameInput}&type=${type}&phase1name=${phase1NameInput}&phase1description=${phase1DescriptionInput}&phase1till=${phase1TillInput}&phase1goal=${phase1GoalInput}&phase2name=${phase2NameInput}&phase2description=${phase2DescriptionInput}&phase2till=${phase2TillInput}&phase2goal=${phase2GoalInput}&phase3name=${phase3NameInput}&phase3description=${phase3DescriptionInput}&phase3till=${phase3TillInput}&phase3goal=${phase3GoalInput}&phase4name=${phase4NameInput}&phase4description=${phase4DescriptionInput}&phase4till=${phase4TillInput}&phase4goal=${phase4GoalInput}&phase1proofOfEvidence=${phase1PoeInput}&phase2proofOfEvidence=${phase2PoeInput}&phase3proofOfEvidence=${phase3PoeInput}&phase4proofOfEvidence=${phase4PoeInput}`,
                "POST"
            )
            .then(data => {
                if(data !== undefined){
                    if (data['error'] !== undefined) {
                        setCollectionSuccess(null)
                        setCollectionError(data['error'])
                    } else {
                        setCollectionError(null)
                        setCollectionSuccess("New colletion has been added!")
                    }
                }
            })
        }
    }

    const GetUsersCollections = async (authToken) => {
        performRequest(
            authToken,
            `/api/collection/owned`,
            "GET"
        )
        .then(data => {
            if(data !== undefined){
            setCollections(data);
            }
        })
    }

    const GetSupportedPhases = async (authToken) => {
        performRequest(
            authToken,
            `/api/web3/fund/availabletoreceive/sustainer`,
            "GET"
        )
        .then(data => {
            if(data !== undefined){
            setSupportedFraudPhases(data);
            }
        })
    }

    const GetOwnedPhases = async (authToken) => {
        performRequest(
            authToken,
            `/api/web3/fund/availabletoreceive/founder`,
            "GET"
        )
        .then(data => {
            if(data !== undefined){
            setOwnedSuccessPhases(data);
            }
        })
    }

    const performRequest = async (authToken, url, method) => {
        return await fetch(`${URL}${url}`, {
            method: method,
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Access-Control-Allow-Origin': "*",
                'Authorization': authToken
            },
        })
        .then(response => {
            if(response.status === 403){
                setAccessError("Access denied. Please log in again.")
                setAuthenticated(false)
            }else{
                return response.json()
            }
        })
        .catch(err => {
            setCollectionError("Request has failed. Try again later!")
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
            DepositFunds,
            AddCollection,
            GetUsersCollections,
            openedCollectionModal,
            setOpenedCollectionModal,
            GetOwnedPhases, 
            GetSupportedPhases,
            supportedFraudPhases,
            ownedSuccessPhases,
            setSupportedFraudPhases,
            setOwnedSuccessPhases,
            PublishCollection,
            DeleteCollection
        }}
        >
            {children}
        </CollectionContext.Provider>
    );
};