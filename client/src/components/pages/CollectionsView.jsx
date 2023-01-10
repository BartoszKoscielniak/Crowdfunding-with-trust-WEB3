import { useContext, useEffect, useState } from "react";
import { CollectionContext } from '../../context/CollectionContext';
import { CollectionCard, ErrorAlert, SuccessAlert, Pagination } from '../../components'

const CollectionsView = () => {
  const { GetAllCollections, collections, collectionError, collectionSuccess, setCollectionError, setCollectionSuccess } = useContext(CollectionContext);

  useEffect(() => {
    GetAllCollections()
  }, [])

  const getCollectionCards = () => {
    let array = [];
    Object.keys(collections).map((key) => {
      array.push(<li key={collections[key]['collectionName']}><CollectionCard arrayId={key} title={collections[key]['collectionName']} onClickFunction = {true} description={collections[key]['baseDescription']} goal={collections[key]['goal']} type={collections[key]['collectionType']} promo = {collections[key]['promoted']} actualFunds = {collections[key]['actualFunds'].toFixed(4)} /></li>);
    });
    return array;
  }

  return (
    <div>
      <div className="bg-neutral-800 w-full absolute mf:flex-row md:px-20 md:py-28 px-4 ">
          {collections !== null ? (
            <div>
              {
                <Pagination items = {getCollectionCards()} collectionsPerPage = {10} rows = {2} cols = {5}/>
              }
            </div>
          ) : (
            <div className="relative grid gap-10 grid-cols-5 grid-rows-2">
              <CollectionCard title={''} description={''} goal={''} type={''} isLoaded={false} />
              <CollectionCard title={''} description={''} goal={''} type={''} isLoaded={false} />
              <CollectionCard title={''} description={''} goal={''} type={''} isLoaded={false} />
              <CollectionCard title={''} description={''} goal={''} type={''} isLoaded={false} />
              <CollectionCard title={''} description={''} goal={''} type={''} isLoaded={false} />
              <CollectionCard title={''} description={''} goal={''} type={''} isLoaded={false} />
            </div>
          )}
          <div className="w-full flex md:justify-center justify-between items-center flex-col pt-4 px-4">
            <div className="sm:w-[90%] w-full h-[0.25px] bg-gray-400 mt-5 " />
              <div className="sm:w-[90%] w-full flex justify-between items-center mt-3">
              <p className="text-white text-left text-xs">@Crowdfunding with trust</p>
              <p className="text-white text-right text-xs">All rights reserved</p>
            </div>
          </div>
          {collectionError !== null && (
            <ErrorAlert text={collectionError} close = {() => {setCollectionError(null)}}/>
          )}
          {collectionSuccess !== null && (
            <SuccessAlert text={collectionSuccess} close = {() => {setCollectionSuccess(null)}}/>
          )}
      </div>        
    </div>
  );
}

export default CollectionsView;