import { useContext, useEffect, useState } from "react";
import { CollectionContext } from '../../context/CollectionContext';
import { AccessContext } from '../../context/AccessContext';
import { PollContext } from "../../context/PollsContext";
import { CollectionCard, ErrorAlert, SuccessAlert, Pagination } from '../../components'

const CollectionsView = () => {
  const { GetUsersCollections, collections, collectionError, collectionSuccess, setCollectionError, setCollectionSuccess, openedCollectionModal } = useContext(CollectionContext);
  const { polls, pollError, Pollsuccess, setPollError, setPollsuccess, GetOwnedPolls } = useContext(PollContext);
  const { authenticated, bearerToken } = useContext(AccessContext);

  useEffect(() => {
    if (!authenticated) {
      setCollectionError("You have to be logged in!")
    } else {
      GetUsersCollections(bearerToken)
    }
  }, [])

  const getCollectionCards = () => {
    let array = [];
    Object.keys(collections).map((key) => {
      array.push(<li key={collections[key]['collectionName']}><CollectionCard arrayId={key} id={collections[key]['id']} title={collections[key]['collectionName']} description={collections[key]['baseDescription']} goal={collections[key]['goal']} type={collections[key]['collectionType']} promo={collections[key]['promoted']} actualFunds={collections[key]['actualFunds'].toFixed(4)} /></li>);
    });
    return array;
  }

  return (
    <div>
      <div className="bg-neutral-800 w-full absolute mf:flex-row md:px-20 md:py-28 px-4 ">
        <div className="">{/* relative grid gap-3 grid-cols-2 grid-rows-1 */}
          <div className="overflow-y-auto px-16 h-[960px] w-1/2 float-left">
            {collections !== null ? (
              <div className="inline-block">
                {
                  <Pagination items={getCollectionCards()} collectionsPerPage={4} rows={2} cols={2} />
                }
              </div>
            ) : (
              <div className="relative grid gap-3 grid-cols-2 grid-rows-1">
                <CollectionCard title={''} description={''} goal={''} type={''} isLoaded={false} />
                <CollectionCard title={''} description={''} goal={''} type={''} isLoaded={false} />
                <CollectionCard title={''} description={''} goal={''} type={''} isLoaded={false} />
                <CollectionCard title={''} description={''} goal={''} type={''} isLoaded={false} />
              </div>
            )}
          </div>
          <div className="h-[960px] p-3 inline-block text-white border rounded-xl w-1/2">
            {openedCollectionModal !== null && collections !== null && (
              <div className="text-lg font-sans, text-white">
                <h1 className="text-3xl text-center">{collections[openedCollectionModal]['collectionName']} - informations</h1><br/>
                <div className="hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150">
                  <p className='text-2xl text-white '>
                      Supporters: {collections[openedCollectionModal]['collUserRelations'].length - 1}
                  </p>
                  <p className='text-2xl text-white '>
                      Funds overall: {collections[openedCollectionModal]['actualFunds']} ETH
                  </p>
                  <p className='text-2xl text-white '>
                      Published: {collections[openedCollectionModal]['state']} 
                  </p>
                  <div>
                  {collections[openedCollectionModal]['promoTo'] !== null ? (
                    <p className='text-2xl text-white'>
                      Promotion: {collections[openedCollectionModal]['promoTo'].substring(0, collections[openedCollectionModal]['promoTo'].indexOf('T'))} 
                    </p>
                  ) : (
                    <p className='text-2xl text-white '>
                      Promotion: No 
                    </p>
                  )}
                  </div>
                </div>
                <br/>
                <div className="hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm max-h-[210px] overflow-auto hover:drop-shadow-2xl duration-150">
                  {Object.keys(collections[openedCollectionModal]['collectionPhase']).map((key) => {
                    return <div key={key}>
                        <div className='font-sans text-2xl antialiased'>
                            <p className='underline inline-block'>{collections[openedCollectionModal]['collectionPhase'][key]['phaseName']}</p>
                            <p className='inline-block float-right'>{collections[openedCollectionModal]['collectionPhase'][key]['actualFunds'].toFixed(4)}/{collections[openedCollectionModal]['collectionPhase'][key]['goal']} ETH</p>
                        </div><br />
                        <div className='font-sans text-md antialiased'>
                            <p>{collections[openedCollectionModal]['collectionPhase'][key]['description']}</p>
                            { collections[openedCollectionModal]['collectionType'] === 'STARTUP' && (
                              <a className='underline-offset-1 inline-block underline' href={`${collections[openedCollectionModal]['collectionPhase'][key]['proofOfEvidence']}`} target='_blank' rel='noopener noreferrer'>Your evidences</a>
                            )}
                            <p>Till: {collections[openedCollectionModal]['collectionPhase'][key]['till'].substring(0, collections[openedCollectionModal]['collectionPhase'][0]['till'].indexOf('T'))}</p><br />
                        </div>
                    </div>
                  })}
                </div>
                <br/>
                <div className="hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm max-h-[160px] overflow-auto hover:drop-shadow-2xl duration-150">
                  {
                    polls !== null && polls.length !== 0 ? (
                      Object.keys(polls).map((key) => {
                        return <div key={key}>
                          <div className='font-sans text-2xl antialiased'>
                            <p className='underline inline-block'>Poll {parseInt(key) + 1}</p>
                            <p className='inline-block float-right'>{polls[key]['state']}</p>
                          </div><br />
                          <div className='font-sans text-md antialiased'>
                            <p>Votes: {polls[key]['votes'].length} / {polls[key]['allowedUsersCount']}</p>
                            <p>Starts: {polls[key]['startDate'].substring(0, polls[key]['startDate'].indexOf('T'))} - {polls[key]['endDate'].substring(0, polls[key]['endDate'].indexOf('T'))}</p><br />
                          </div>
                        </div>
                      })
                      ) : (
                        <div>
                          { collections[openedCollectionModal]['collectionType'] === 'STARTUP' ? (
                            <p className="text-center text-2xl">Polls will be created after first donation to phase</p>
                          ) : (
                            <p className="text-center text-2xl">Polls will be created after first donation to collection</p>
                          )}
                        </div>
                      )
                  }
                </div>
              </div>
            )}
          </div>
        </div>
        <div className="w-full flex md:justify-center justify-between items-center flex-col pt-4 px-4">
          <div className="sm:w-[90%] w-full h-[0.25px] bg-gray-400 mt-5 " />
          <div className="sm:w-[90%] w-full flex justify-between items-center mt-3">
            <p className="text-white text-left text-xs">@Crowdfunding with trust</p>
            <p className="text-white text-right text-xs">All rights reserved</p>
          </div>
        </div>
        {collectionError !== null && (
          <ErrorAlert text={collectionError} close={() => { setCollectionError(null) }} />
        )}
        {collectionSuccess !== null && (
          <SuccessAlert text={collectionSuccess} close={() => { setCollectionSuccess(null) }} />
        )}
        {pollError !== null && (
          <ErrorAlert text={pollError} close={() => { setPollError(null) }} />
        )}
        {Pollsuccess !== null && (
          <SuccessAlert text={Pollsuccess} close={() => { setPollsuccess(null) }} />
        )}
      </div>
    </div>
  );
}

export default CollectionsView;