import React, {useEffect, useState, useContext} from "react";
import { AccessContext } from "./AccessContext";

export const AccountContext = React.createContext();

export const AccountProvider = ({children}) => {
    
    const URL = 'http://localhost:8080';
    const [accountError, setAccountErrorState]           = useState(null);
    const [accountSuccess, setAccountSuccessState]       = useState(null);
    const [userData, setUserData]                        = useState(null)
    const { setAuthenticated, setAccessError }           = useContext(AccessContext);

    const setAccountError = (msg) => {
        setAccountSuccessState(null)
        setAccountErrorState(msg)
    }

    const setAccountSuccess = (msg) => {
        setAccountSuccessState(msg)
        setAccountErrorState(null)
    }

    const GetMyInformation = async (authToken) => {
        performRequest(
            authToken,
            `/api/user/me`,
            "GET"
        )
        .then(data => {
            if(data !== undefined){
                setUserData(data);
            }
        })
    }

    const ChangePassword = async (authToken, oldPassword, newPassword) => {
        performRequest(
            authToken,
            `/api/user/password?oldPassword=${oldPassword}&newPassword=${newPassword}`,
            "PUT"
        )
        .then(data => {
            if(data !== undefined){
                if(data['error'] === undefined){
                    setAccountSuccess(data['result'])
                }else{
                    setAccountError(data['error'])
                }
            }
        })
    }

    const ChangeDetails = async (authToken, name, lastname) => {
        performRequest(
            authToken,
            `/api/user/details?name=${name}&lastname=${lastname}`,
            "PUT"
        )
        .then(data => {
            if(data !== undefined){
                if(data !== undefined && data['error'] === undefined){
                    setAccountSuccess(data['result'])
                }else{
                    setAccountError(data['error'])
                }
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
            setAccountError(err.message)
        });
    }

    useEffect(() => {

    }, []);

    return (
        <AccountContext.Provider value={{ 
            accountError,
            setAccountError,
            accountSuccess,
            setAccountSuccess,
            userData,
            setUserData,
            GetMyInformation,
            ChangePassword,
            ChangeDetails
            }}
            >
            {children}
        </AccountContext.Provider>
    );
};