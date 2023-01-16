import { useContext, useEffect, useState } from "react";
import { CollectionContext } from '../../context/CollectionContext';
import { AccessContext } from '../../context/AccessContext';
import { PollContext } from "../../context/PollsContext";
import { CollectionCard, ErrorAlert, SuccessAlert, Pagination, Input } from '..'
import { Navigate } from "react-router-dom";

const CollectionsView = () => {
  const { polls, pollError, Pollsuccess, setPollError, setPollsuccess, GetAccessiblePolls, Vote } = useContext(PollContext);
  const { authenticated, bearerToken, login, setAccessError } = useContext(AccessContext);
  const [loadingSpinner, setLoadingSpinner]   = useState(false)
  
  useEffect(() => {
    if (!authenticated) {
      setPollError("You have to be logged in!")
      setAccessError("You have to be logged in!")
    } else {
      GetAccessiblePolls(bearerToken)
    }
  }, [])

  return (
    <div>{!authenticated ? (<Navigate replace to="/" />) : (
      <div className="bg-neutral-800 w-full absolute mf:flex-row md:px-20 md:py-28 px-4 h-full">
        <h1 className="text-white text-3xl px-4 pb-2">Ongoing polls</h1>
        <div className='mx-2 text-white text-md px-2 max-h-[520px] overflow-auto border rounded-xl p-3 drop-shadow-2xl'>
            {
              polls !== null && polls.length !== 0 ? (
                Object.keys(polls).map((key) => {
                    return <div key={key} className='hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150'>
                        <div className='font-sans text-2xl antialiased py-1'>
                            <p className='underline inline-block'>{polls[key]['collectionPhase']['phaseName']} - Poll</p>
                            <p className='inline-block float-right'>Status: {polls[key]['state']}</p>
                        </div><br />
                        <div className='font-sans text-md antialiased text-xl'>
                            <p>Votes: {polls[key]['votes'].length} / {polls[key]['allowedUsersCount']}</p>
                            <p>Starts: {polls[key]['startDate'].substring(0, polls[key]['startDate'].indexOf('T'))} - {polls[key]['endDate'].substring(0, polls[key]['endDate'].indexOf('T'))}</p>
                            <p>Proof of evidence: <a className='underline-offset-1 inline-block underline' href={polls[key]['collectionPhase']['proofOfEvidence']} target='_blank' rel='noopener noreferrer'>Evidences</a></p><br />
                            
                            { polls[key]['state'] === 'IN_PROCESS' && (
                              <div>
                              <button
                              onClick={() => Vote(bearerToken, polls[key]['collectionPhase']['id'], 'ACCEPTED')}
                              className="text-white text-xl p-2 mx-1 mb-1 rounded-lg bg-green-700 transition ease-in-out delay-50 hover:scale-105 hover:bg-green-500 duration-200"
                              >
                                Release funds
                              </button>
                              <button
                              onClick={() => Vote(bearerToken, polls[key]['collectionPhase']['id'], 'DECLINED')}
                              className="text-white text-xl p-2 mx-1 mb-1 rounded-lg bg-red-700 transition ease-in-out delay-50 hover:scale-105 hover:bg-red-500 duration-200"
                              >
                                Return fudns
                              </button>
                              </div>
                            )}
                        </div>
                    </div>
                })
              ) : (
                <div>
                  <p className='font-sans text-2xl antialiased text-center'>No ongoing polls to be displayed here</p>
                </div>
              )
            }
        </div>
        <div className="w-full flex md:justify-center justify-between items-center flex-col pt-4 px-4">
          <div className="sm:w-[90%] w-full h-[0.25px] bg-gray-400 mt-5 " />
          <div className="sm:w-[90%] w-full flex justify-between items-center mt-3">
            <p className="text-white text-left text-xs">@Crowdfunding with trust</p>
            <p className="text-white text-right text-xs">All rights reserved</p>
          </div>
        </div>
        {pollError !== null && (
          <ErrorAlert text={pollError} close={() => { setPollError(null) }} />
        )}
        {Pollsuccess !== null && (
          <SuccessAlert text={Pollsuccess} close={() => { setPollsuccess(null) }} />
        )}
      </div>
    )}

    </div>
  );
}

export default CollectionsView;