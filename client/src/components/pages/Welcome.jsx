import React, { useContext } from "react";
import { Card } from '../index'
import { BsMouse } from "react-icons/bs";
import { AccessContext } from "../../context/AccessContext";
import { ErrorAlert} from '../../components'


const Welcome = () => {
  const { accessError, setAccessError } = useContext(AccessContext);

  return (
    <div>
      <div className="absolute mf:flex-row md:p-20 py-12 px-4 top-1/2 -translate-y-1/2">
        <div className="mf:mr-10">
          <h1 className="font-sans flex w-full justify-start text-white text-3xl">
            Welcome to Crowdfunding with trust
          </h1>
          <h1 className="font-sans flex w-full justify-start text-white text-7xl py-5">
            Where we care <br />about Your confidence.
          </h1>
          <p className="font-sans flex w-full justify-start text-white text-3xl">
            Explore the crypto world while supporting startups.
          </p>
        </div>
      </div>
      <div className="absolute bottom-10 right-1/2 -translate-x-1/2 animate-bounce">
        <BsMouse fontSize={35} className="text-white" />
      </div>
      <section id="aboutus" className="absolute flex-col flex-1 items-center justify-start w-full mf:mt-0 mt-40 align-center px-20 -bottom-[90%]">
        <div className="inline-table pb-30">
          <div className="w-1/2 table-cell pr-1">
            <Card
              color={'bg-pink-900'}
              title={'Low commission'}
              text={'At as lowest level as 0.1% of deposited amount. Charity collection are not affected by commission!'}
            />
          </div>
          <div className="w-1/2 table-cell pl-1">
            <Card
              color={'bg-teal-800'}
              title={'Protected funds'}
              text={'Your funds are stored in escrow, preventing startup collection owner from stealing Your crypto without fulfilling collection assumptions.'}
            />
          </div>
        </div>
        <div className="inline-table pb-30">
          <div className="w-2/3 table-cell pr-1">
            <Card
              color={'bg-green-900'}
              title={'Security & Data Backup'}
              text={'Blockchain do not forget! Even in case of service disaster, blockchain will store Your funds without any loss.'}
            />
          </div>
          <div className="w-1/3 table-cell pl-1">
            <Card
              color={'bg-indigo-900'}
              title={'Charity & Startups'}
              text={'We support both types to meet all of Your demands.'}
            />
          </div>
        </div>
        <div className="w-3/5">
        <Card
              color={'bg-indigo-800	'}
              title={'Public contracts'}
              text={'Means You can check contract code on blockchain and see how We works!'}
            />    
        </div>  
{/*         {accessError !== null && (          
          <ErrorAlert text={accessError} close={() => { setAccessError(null); }} />
        )} */}
        <div className=" w-full flex md:justify-center justify-between items-center flex-col pt-4 px-4">
          <div className="sm:w-[90%] w-full h-[0.25px] bg-gray-400 mt-5 " />
          <div className="sm:w-[90%] w-full flex justify-between items-center mt-3">
            <p className="text-white text-left text-xs">@Crowdfunding with trust</p>
            <p className="text-white text-right text-xs">All rights reserved</p>
          </div>
        </div>
      </section>
    </div>
  );
}

export default Welcome;