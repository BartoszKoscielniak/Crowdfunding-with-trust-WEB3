import React, { useEffect, useState } from "react";

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
    const [pollError, setPollError]             = useState(null)
    const [Pollsuccess, setPollsuccess]         = useState(null)
    const [authenticated, setAuthenticated]     = sessionStorageState('authenticated', false);

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
            .then(response => response.json())
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
        .then(response => response.json())
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
            .then(response => response.json())
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
        .then(response => response.json())
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