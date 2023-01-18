import React, {useEffect, useState} from "react";
import { ethers } from 'ethers';

export const AccessContext = React.createContext();

const { ethereum } = window;

const sessionStorageState = (key, receivedValue) => {
    const [value, setValue] = useState(JSON.parse(sessionStorage.getItem(key)) ?? receivedValue);

    useEffect(() => {
        sessionStorage.setItem(key, JSON.stringify(value));
    }, [value, key]);
    
    return [value, setValue];
}

export const AccessProvider = ({children}) => {
    
    const URL = 'http://localhost:8080';
    const [login, setLogin]                     = sessionStorageState('login', "");
    const [password, setPassword]               = useState("");
    const [registerData, setRegisterData]       = useState({nameInput: '', surnameInput: '', emailInput: '', phoneInput: '', keyInput: '', passwordInput: ''})
    const [accessError, setAccessErrorState]         = useState(null);
    const [accessSuccess, setAccessSuccessState]     = useState(null)
    const [authenticated, setAuthenticated]     = sessionStorageState('authenticated', false);
    const [bearerToken, setBearerToken]         = sessionStorageState('authToken', null);

    const setAccessError = (msg) => {
        setAccessSuccessState(null)
        setAccessErrorState(msg)
    }

    const setAccessSuccess = (msg) => {
        setAccessSuccessState(msg)
        setAccessErrorState(null)
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

    const PerformLogin = async () => {
        try {
            fetch(`${URL}/login`, {
                method: 'POST',
                body: JSON.stringify({
                    "publicaddress": login,
                    "password": password,
                }),
                headers: {
                    'Content-type': 'application/json; charset=UTF-8',
                    'Access-Control-Allow-Origin': "*"
                },
                })
                .then(response => {
                    if (response.status === 200){
                        setAuthenticated(true);
                    }else {
                        setAccessError("Invalid login or password");
                    }
                    setBearerToken(response.headers.get('Authorization'))
                })
                .catch(err => {
                    setAccessError(err.message);
                });  
        } catch (error) {
            setAccessError(error.message)
        }
    }

    const Logout = () => {
        setPassword("");
        setBearerToken(null);
        setAuthenticated(false);
    }

    const PerformRegistration = () => {
        const {nameInput, surnameInput, emailInput, phoneInput, keyInput, passwordInput} = registerData;
        try {
            fetch(`${URL}/api/user/register?name=${nameInput}&surname=${surnameInput}&privateKey=${keyInput}&password=${passwordInput}&email=${emailInput}&phoneNumber=${phoneInput}`, {
                method: 'POST',
                headers: {
                    'Content-type': 'application/json;',
                    'Access-Control-Allow-Origin': "*"
                },
            })
            .then(response => response.json())
            .then(data => {
                if(data['error'] === undefined){
                    setAccessSuccess(data['result'])
                }else{
                    throw Error(data['error'])
                }
            })
            .catch(err => {
                setAccessError(err.message);
            });
        } catch (error) {console.log(error.message)
            setAccessError(error.message);
        }
    }

    useEffect(() => {

    }, []);

    return (
        <AccessContext.Provider value={{ 
            PerformLogin, 
            login,
            password,
            handleChange,
            handleChangeRegister,
            accessError,
            setAccessError,
            authenticated,
            Logout,
            PerformRegistration,
            registerData,
            accessSuccess,
            setAccessSuccess,
            bearerToken,
            setAuthenticated
            }}
            >
            {children}
        </AccessContext.Provider>
    );
};