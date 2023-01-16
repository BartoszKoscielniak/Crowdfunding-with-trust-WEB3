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
      <section id="aboutus" className="absolute flex-col flex-1 items-center justify-start w-full mf:mt-0 mt-40 align-center px-20 -bottom-[96%] pt-10">
        <div className="w-full table-cell pr-1">
            <Card
              color={'bg-slate-700'}
              title={'What we do?'}
              content={<p className="font-sans leading-normal text-2xl pb-10 inline-block text-white">We provide Crowdfunding system which is integratet with Blockchain technology. It emphasize on security to eliminate risk of being tricked to donate impostor. System consist of contracts which operates with Your funds. With that being said, the most important and trust demanding functionalities are in Your hands. Smart contract code is visible all the time so You can check it and choose, whether it is trustful or not.</p>}
            />
        </div>
        <div className="inline-table pb-30">
          <div className="w-1/2 table-cell pr-1">
            <Card
              color={'bg-pink-900'}
              title={'Low commission'}
              content={<p className="font-sans leading-normal text-2xl pb-10 inline-block text-white">At as lowest level as 0.1% of deposited amount. Charity collection are not affected by commission!</p>}
            />
          </div>
          <div className="w-1/2 table-cell pl-1">
            <Card
              color={'bg-teal-800'}
              title={'Protected funds'}
              content={<p className="font-sans leading-normal text-2xl pb-10 inline-block text-white">Your funds are stored in escrow, preventing startup collection owner from stealing Your crypto without fulfilling collection assumptions.</p>}
            />
          </div>
        </div>
        <div className="inline-table pb-30">
          <div className="w-2/3 table-cell pr-1">
            <Card
              color={'bg-green-900'}
              title={'Security & Data Backup'}
              content={<p className="font-sans leading-normal text-2xl pb-10 inline-block text-white">Blockchain do not forget! Even in case of service disaster, blockchain will store Your funds without any loss.</p>}
            />
          </div>
          <div className="w-1/3 table-cell pl-1">
            <Card
              color={'bg-indigo-900'}
              title={'Charity & Startups'}
              content={<p className="font-sans leading-normal text-2xl pb-10 inline-block text-white">We support both types to meet all of Your demands.</p>}
            />
          </div>
        </div>
        <div className="w-3/5">
        <Card
              color={'bg-indigo-800	'}
              title={'Public contracts'}
              content={<div><p className="font-sans leading-normal text-2xl pb-10 text-white">Means You can check contract code on blockchain and see how We works!</p>
              <a className='underline-offset-1 underline text-2xl p-2 text-white' href={`https://goerli.etherscan.io/address/0x4c8fd932918ab2d546dfa6c8094f3712150e72a6`} target='_blank' rel='noopener noreferrer'>Funds contract</a>
              <a className='underline-offset-1 underline text-2xl p-2 text-white' href={`https://goerli.etherscan.io/address/0x2ddf9ed285d762736917747694ed036851dfeaf4`} target='_blank' rel='noopener noreferrer'>Commission contract</a>
              <a className='underline-offset-1 underline text-2xl p-2 text-white' href={`https://goerli.etherscan.io/address/0x13a42739c1d18b49cd818aa8c4d6247a7f383487`} target='_blank' rel='noopener noreferrer'>Advertise contract</a>
              </div>
            }
            />    
        </div>  
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