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
    const [pollError, setPollErrorState]        = useState(null)
    const [Pollsuccess, setPollsuccessState]    = useState(null)
    const { setAuthenticated, setAccessError }  = useContext(AccessContext);

    const setPollError = (msg) => {
        setPollsuccessState(null)
        setPollErrorState(msg)
    }

    const setPollsuccess = (msg) => {
        setPollsuccessState(msg)
        setPollErrorState(null)
    }

    const GetPollById = async (authToken, collectionId) => {
        if (collectionId !== undefined){
            performRequest(
                authToken,
                `/api/poll/collectionpolls?collectionid=${collectionId}`,
                "GET"
            )
            .then(data => {
                setPolls(data);
            })
        }
    }

    const GetPollHistory = async (authToken) => {
        performRequest(
            authToken,
            `/api/poll/pollshistory`,
            "GET"
        )
        .then(data => {
            setPolls(data);
        })
    }

    const Vote = async (authToken, phaseId, result) => {
        performRequest(
            authToken,
            `/api/vote?phaseId=${phaseId}&result=${result}`,
            "POST"
        )
        .then(data => {
            if (data['error'] !== undefined) {
                setPollError(data['error'])
            } else {
                setPollsuccess(<div><p className='inline-block'>{data['result']}</p></div>)
                GetAccessiblePolls(authToken)
            }
        })
    }

    const GetAccessiblePolls = async (authToken) => {
        performRequest(
            authToken,
            `/api/poll/usersaccessible`,
            "GET"
        )
        .then(data => {
            setPolls(data);
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
            setPollError("Request has failed. Try again later!")
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