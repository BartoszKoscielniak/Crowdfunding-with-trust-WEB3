import { useContext, useEffect, useState } from "react";
import { CollectionContext } from '../../context/CollectionContext';
import { AccessContext } from '../../context/AccessContext';
import { CollectionCard, ErrorAlert, SuccessAlert, Pagination } from '../../components'

const CollectionsView = () => {
  const { GetUsersCollections, collections, collectionError, collectionSuccess, setCollectionError, setCollectionSuccess, openedCollectionModal } = useContext(CollectionContext);
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
      array.push(<li key={collections[key]['collectionName']}><CollectionCard arrayId={key} title={collections[key]['collectionName']} description={collections[key]['baseDescription']} goal={collections[key]['goal']} type={collections[key]['collectionType']} promo={collections[key]['promoted']} actualFunds={collections[key]['actualFunds'].toFixed(4)} /></li>);
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
            {openedCollectionModal !== null && (
              <div className="text-lg font-sans, text-white">
                <h1 className="text-2xl text-center">{openedCollectionModal['collectionName']} - statistics</h1>
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
      </div>
    </div>
  );
}

export default CollectionsView;