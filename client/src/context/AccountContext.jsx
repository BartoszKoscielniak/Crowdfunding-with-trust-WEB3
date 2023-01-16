import React, {useEffect, useState, useContext} from "react";
import { AccessContext } from "./AccessContext";

export const AccountContext = React.createContext();

export const AccountProvider = ({children}) => {
    
    const URL = 'http://localhost:8080';
    const [accountError, setAccountErrorState]           = useState(null);
    const [accountSuccess, setAccountSuccessState]       = useState(null);
    const [userData, setUserData]                   = useState(null)
    const { setAuthenticated, setAccessError }      = useContext(AccessContext);

    const setAccountError = (msg) => {
        setAccountSuccessState(null)
        setAccountErrorState(msg)
    }

    const setAccountSuccess = (msg) => {
        setAccountSuccessState(msg)
        setAccountErrorState(null)
    }

    const handleChange = (e, name) => {
        if(name == 'loginInput'){
            setLogin(e.target.value);
        }

        if(name == 'passwordInput'){
            setPassword(e.target.value);
        }
    }

    const handleChangeRegister = (e, name) => {
        setRegisterData((prevState) => ({...prevState, [name]: e.target.value}));
    }

    const GetMyInformation = async (authToken) => {
        return await fetch(`${URL}/api/user/me`, {
            method: 'GET',
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
        .then(data => {
            setUserData(data);
        })
        .catch(err => {
            setAccountError(err.message)//setAccountError("Failed to fetch collections. Try again later!")
        });
    }

    const ChangePassword = async (authToken, oldPassword, newPassword) => {
        return await fetch(`${URL}/api/user/password?oldPassword=${oldPassword}&newPassword=${newPassword}`, {
            method: 'PUT',
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
            .then(data => {
                if(data['error'] === undefined){
                    setAccountSuccess(data['result'])
                }else{
                    throw Error(data['error'])
                }
            })
            .catch(err => {
                setAccountError(err.message)
            });
    }

    const ChangeDetails = async (authToken, name, lastname) => {
        return await fetch(`${URL}/api/user/details?name=${name}&lastname=${lastname}`, {
            method: 'PUT',
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
            .then(data => {
                if(data['error'] === undefined){
                    setAccountSuccess(data['result'])
                }else{
                    throw Error(data['error'])
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