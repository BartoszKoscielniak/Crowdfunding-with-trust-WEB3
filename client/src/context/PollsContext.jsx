import React, { useEffect, useState, useContext } from "react";
import { AccessContext } from "./AccessContext";

export const PollContext = React.createContext();

const sessionStorageState = (key, receivedValue) => {
    const [value, setValue] = useState(JSON.parse(sessionStorage.getItem(key)) ?? receivedValue);

    useEffect(() => {
        sessionStorage.setItem(key, JSON.stringify(value));
    }, [value, key]);
    
    return [value, setValue];
}

export const PollProvider = ({ children }) => {

    const URL = 'http://localhost:8080';
    const [polls, setPolls]                     = useState(null)
    const [pollError, setPollErrorState]             = useState(null)
    const [Pollsuccess, setPollsuccessState]         = useState(null)
    const { setAuthenticated, setAccessError }  = useContext(AccessContext);

    const setPollError = (msg) => {
        setPollsuccessState(null)
        setPollErrorState(msg)
    }

    const setPollsuccess = (msg) => {
        setPollsuccessState(msg)
        setPollErrorState(null)
    }

    const handleChangeRegister = (e, name) => {
        setRegisterData((prevState) => ({ ...prevState, [name]: e.target.value }));
    }

    const GetPollById = async (authToken, collectionId) => {
        if (collectionId !== undefined){
            return await fetch(`${URL}/api/poll/collectionpolls?collectionid=${collectionId}`, {
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
                setPolls(data);
            })
            .catch(err => {
                setPollError("Failed to fetch polls. Try again later!")
            });
        }
    }

    const GetPollHistory = async (authToken) => {
        return await fetch(`${URL}/api/poll/pollshistory`, {
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
            setPolls(data);
        })
        .catch(err => {
            setPollError("Failed to fetch polls. Try again later!")
        });
    }

    const Vote = async (authToken, phaseId, result) => {
        return await fetch(`${URL}/api/vote?phaseId=${phaseId}&result=${result}`, {
            method: 'POST',
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
                if (data['error'] !== undefined) {
                    setPollError(data['error'])
                } else {
                    setPollsuccess(<div><p className='inline-block'>{data['result']}</p></div>)
                    GetAccessiblePolls(authToken)
                }
            })
            .catch(err => {
                setPollError(err.message)
            });
    }

    const GetAccessiblePolls = async (authToken) => {
        return await fetch(`${URL}/api/poll/usersaccessible`, {
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
            setPolls(data);
        })
        .catch(err => {
            setPollError("Failed to fetch polls. Try again later!")
        });
    }

    useEffect(() => {

    }, []);

    return (
        <PollContext.Provider value={{
            polls,
            pollError,
            Pollsuccess,
            setPollError,
            setPollsuccess,
            GetPollById,
            GetAccessiblePolls,
            Vote,
            GetPollHistory
        }}
        >
            {children}
        </PollContext.Provider>
    );
};