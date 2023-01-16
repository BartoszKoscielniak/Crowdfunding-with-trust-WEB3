import { useContext, useEffect, useState } from "react";
import { CollectionContext } from '../../context/CollectionContext';
import { AccessContext } from '../../context/AccessContext';
import { Web3Context } from '../../context/Web3Context';
import { PollContext } from "../../context/PollsContext";
import { AiOutlineLoading3Quarters } from "react-icons/ai";
import { CollectionCard, ErrorAlert, SuccessAlert, Pagination } from '../../components'
import { Navigate } from "react-router-dom";
import { Input } from '..';


const CollectionsView = () => {
  const { GetUsersCollections, collections, collectionError, collectionSuccess, setCollectionError, setCollectionSuccess, openedCollectionModal, PublishCollection, DeleteCollection } = useContext(CollectionContext);
  const { polls, pollError, Pollsuccess, setPollError, setPollsuccess } = useContext(PollContext);
  const { authenticated, bearerToken, setAccessError } = useContext(AccessContext);
  const { GetAdvertiseTypes, adTypes, Web3Error, setWeb3Error, Web3Success, setWeb3Success, BuyAdvertise} = useContext(Web3Context)
  const [promoPanel, setPromoPanel] = useState(false)
  const [radioselected, setRadioselected] = useState("0")
  const [loadingSpinner, setLoadingSpinner] = useState(false)

  useEffect(() => {
    if (!authenticated) {
      setCollectionError("You have to be logged in!")
      setAccessError("You have to be logged in!")
    } else {
      GetUsersCollections(bearerToken)
      GetAdvertiseTypes(bearerToken)
    }
  }, [])

  const getCollectionCards = () => {
    let array = [];
    Object.keys(collections).map((key) => {
      array.push(<li key={collections[key]['collectionName']}><CollectionCard arrayId={key} id={collections[key]['id']} title={collections[key]['collectionName']} description={collections[key]['baseDescription']} goal={collections[key]['goal']} type={collections[key]['collectionType']} promo={collections[key]['promoted']} actualFunds={collections[key]['actualFunds'].toFixed(4)} /></li>);
    });
    return array;
  }

  const handleRadioChange = (e, name) => {
    setRadioselected(name)
  }

  const isSelected = (name) => {
    return name === radioselected
  }

  return (
    <div>{!authenticated ? (<Navigate replace to="/" />) : (
      <div className="bg-neutral-800 w-full absolute mf:flex-row md:px-20 md:py-28 px-4 ">
        <div className="">{/* relative grid gap-3 grid-cols-2 grid-rows-1 */}
          <div className="overflow-y-auto px-16 h-[960px] max-h-[960px] w-1/2 float-left">
            {collections !== null ? (
              <div className="inline-block w-full max-w-">
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
                <h1 className="text-3xl text-center break-words">{collections[openedCollectionModal]['collectionName']} - informations</h1><br />
                <div className="hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150">
                  <div>
                    <p className='text-2xl text-white mb-1 inline-block'>
                      Supporters: {collections[openedCollectionModal]['collUserRelations'].length - 1}
                    </p>
                    {collections[openedCollectionModal]['collUserRelations'].length === 1 && (
                      <button
                      onClick={() => {
                        DeleteCollection(bearerToken, collections[openedCollectionModal]['id'])
                      }}
                      className="inline-block text-white rounded-xl text-2xl float-right p-1 bg-red-700 transition ease-in-out delay-50 hover:bg-red-500 duration-200"
                    >
                      Delete collection
                    </button>
                    )}
                  </div>
                  <p className='text-2xl text-white mb-1'>
                    Funds overall: {collections[openedCollectionModal]['actualFunds']} ETH
                  </p>
                  <div>
                    {collections[openedCollectionModal]['state'] === 'PUBLISHED' ? (
                        <p className="text-2xl text-white mb-1">Published: {collections[openedCollectionModal]['state']}</p>
                    ) : (
                      <div className="mb-1">
                        <p className="text-2xl text-white inline-block mr-3">Publish:</p>
                        <button
                          onClick={() => {PublishCollection(bearerToken, collections[openedCollectionModal]['id'])}}
                          className="inline-block text-white rounded-xl p-1 bg-green-700 transition ease-in-out delay-50 hover:bg-green-500 duration-200"
                        >
                          Publish
                        </button>
                      </div>
                    )}
                  </div>
                  <div>
                    {collections[openedCollectionModal]['promoTo'] !== null ? (
                      <p className='text-2xl text-white'>
                        Promotion: {collections[openedCollectionModal]['promoTo'].substring(0, collections[openedCollectionModal]['promoTo'].indexOf('T'))}
                      </p>
                    ) : (
                      <div>
                        <p className='text-2xl mr-3 text-white inline-block'>
                          Promotion:
                        </p>
                        {adTypes !== null ? (
                          <button
                          onClick={() => {setPromoPanel(!promoPanel)}}
                          className="inline-block text-white rounded-xl p-1 bg-green-700 transition ease-in-out delay-50 hover:bg-green-500 duration-200"
                        >
                          Promote
                        </button>
                        ) : (
                          <p className='text-2xl mr-3 text-white inline-block'>No</p>
                        )}
                      </div>
                    )}
                  </div>
                </div>
                <br />
                {promoPanel && adTypes !== null && (
                  <div>
                    <div className="hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150">
                    {Object.keys(adTypes).map((key) => {
                      return <div key={key} className='text-xl p-1'>
                        <div className="inline-block mr-2"><Input name={key} type="radio" checked={isSelected(key)} handleChange={handleRadioChange}/></div>
                        <p className="inline-block">{adTypes[key]['name']}</p>
                        <p className="">Duration: {adTypes[key]['duration']} day(s)</p>
                        <p className="">Price: {adTypes[key]['price']} ETH</p>
                      </div>
                  })}
                    <button
                    onClick={() => {
                      setLoadingSpinner(true);
                      BuyAdvertise(bearerToken, collections[openedCollectionModal]['id'], radioselected);
                    }}
                    className="w-full inline-block text-white text-xl rounded-xl p-1 my-2 bg-green-700 transition ease-in-out delay-50 hover:bg-green-500 duration-200"
                    >
                    {loadingSpinner && Web3Error === null && Web3Success === null ? (
                      <AiOutlineLoading3Quarters fontSize={30} className="text-white inline-block animate-spin" />
                    ) : (
                      <p>Submit</p> 
                    )}
                    </button>
                    </div>
                    <br />
                  </div>
                )}
                <div className="hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm max-h-[250px] overflow-auto hover:drop-shadow-2xl duration-150">
                  {Object.keys(collections[openedCollectionModal]['collectionPhase']).map((key) => {
                    return <div key={key} className='break-words'>
                      <div className='font-sans text-2xl antialiased'>
                        <p className='underline inline-block'>{collections[openedCollectionModal]['collectionPhase'][key]['phaseName']}</p>
                        <p className=''>{collections[openedCollectionModal]['collectionPhase'][key]['actualFunds'].toFixed(4)}/{collections[openedCollectionModal]['collectionPhase'][key]['goal']} ETH</p>
                      </div><br />
                      <div className='font-sans text-md antialiased'>
                        <p>{collections[openedCollectionModal]['collectionPhase'][key]['description']}</p>
                        {collections[openedCollectionModal]['collectionType'] === 'STARTUP' && (
                          <a className='underline-offset-1 inline-block underline' href={`${collections[openedCollectionModal]['collectionPhase'][key]['proofOfEvidence']}`} target='_blank' rel='noopener noreferrer'>Your evidences</a>
                        )}
                        <p>Till: {collections[openedCollectionModal]['collectionPhase'][key]['till'].substring(0, collections[openedCollectionModal]['collectionPhase'][0]['till'].indexOf('T'))}</p><br />
                      </div>
                    </div>
                  })}
                </div>
                <br />
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
                        {collections[openedCollectionModal]['collectionType'] === 'STARTUP' ? (
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
          <ErrorAlert text={collectionError} close={() => { setCollectionError(null); setLoadingSpinner(false);}} />
        )}
        {collectionSuccess !== null && (
          <SuccessAlert text={collectionSuccess} close={() => { setCollectionSuccess(null); setLoadingSpinner(false);}} />
        )}
        {pollError !== null && (
          <ErrorAlert text={pollError} close={() => { setPollError(null); setLoadingSpinner(false); }} />
        )}
        {Pollsuccess !== null && (
          <SuccessAlert text={Pollsuccess} close={() => { setPollsuccess(null); setLoadingSpinner(false); }} />
        )}
        {Web3Error !== null && (
          <ErrorAlert text={Web3Error} close={() => { setWeb3Error(null); setLoadingSpinner(false); }} />
        )}
        {Web3Success !== null && (
          <SuccessAlert text={Web3Success} close={() => { setWeb3Success(null); setLoadingSpinner(false); }} />
        )}
      </div>
    )}
    </div>
  );
}

export default CollectionsView;