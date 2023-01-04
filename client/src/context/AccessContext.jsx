import React, {useEffect, useState} from "react";
import { ethers } from 'ethers';

export const AccessContext = React.createContext();

const { ethereum } = window;

const sessionStorageState = (key, receivedValue) => {
    const [value, setValue] = useState(JSON.parse(localStorage.getItem(key)) ?? receivedValue);

    useEffect(() => {
      localStorage.setItem(key, JSON.stringify(value));
    }, [value, key]);
    
    return [value, setValue];
}

export const AccessProvider = ({children}) => {

    const [login, setLogin]                     = sessionStorageState('login', "");
    const [password, setPassword]               = useState("");
    const [registerData, setRegisterData]       = useState({nameInput: '', surnameInput: '', emailInput: '', phoneInput: '', keyInput: '', passwordInput: ''})
    const [accessError, setAccessError]         = useState(null);
    const [authenticated, setAuthenticated]     = sessionStorageState('authenticated', false);

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

    const SignNonce = async (login, password) => {
        return await fetch(`http://localhost:8080/api/user/nonce?publicAddress=${login}&password=${password}`, {
            method: 'GET',
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Access-Control-Allow-Origin': "*"
            },
        })
        .then(response => response.json())
        .then(data => {
            return data;
        })
    }

    const PerformLogin = async () => {
        try {
            if(!ethereum) return alert("Please install Metamask");
            SignNonce(login, password).then((nonceResponse) => {
                try {
                    if(login === "" || password === "") throw Error("All fields must be filled!")
                    if(nonceResponse['error'] !== undefined) {
                        throw Error(nonceResponse['error'])
                    }
                    
                    const provider = new ethers.providers.Web3Provider(ethereum);                        
                    const signature = (provider.getSigner().signMessage(nonceResponse['result'])).then((response) => response).then((data) => {return data});
                    signature.then((b) =>{
                    fetch('http://localhost:8080/login', {
                        method: 'POST',
                        body: JSON.stringify({
                            "publicaddress": login,
                            "password": password,
                            "signature": b
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
                                setAccessError("Invalid signature!");
                            }
                        })
                        .catch(err => {
                            setAccessError(err.message);
                        });
                    }); 
                } catch (error) {
                    setAccessError(error.message)
                }
            });
        } catch (error) {
            setAccessError(error.message)
        }
    }

    const Logout = () => {
        //setLogin("");
        setPassword("");
        setAuthenticated(false);
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
            PerformRegistration
            }}
            >
            {children}
        </AccessContext.Provider>
    );
};


