import { useState, useContext } from 'react';
import { HiMenu, HiMenuAlt4 } from 'react-icons/hi';
import { AiOutlineClose } from 'react-icons/ai'
import { TransactionContext } from '../../context/TransactionContext';
import { AiFillPlayCircle } from "react-icons/ai";

import { Link, NavLink } from 'react-router-dom';

import logo from '../../../images/logo.png';

const NavbarItem = ({ item, classProps}) => {
    const [isShown, setIsShown] = useState(false);

    return (
        <li className={`mx-4 cursor-pointer display: flex justify-center font-sans text-xl antialiased  ${classProps}`} onMouseEnter={() => setIsShown(true)} onMouseLeave={() => setIsShown(false)}>
            <Link to={item[1]}>{item[0]}</Link>   
            {isShown && (
                <DropdownMenu content={item[2]}/>
            )}
        </li>
    );
}

const Navbar = () => {
    const [toggleMenu, setToggleMenu] = useState(false); 
    const routingArray = [
        ["Your account", "account", []],
        ["Collections", "collections", ['Add collections', 'My collections']],
        ["Funds", "funds", ['Deposit', 'History']],
        ["Earn", "earn", ['Profits']],
        ["About us", []]
    ];
    const { connectWallet, currentAccount, formData, sendTransaction, handleChange, isLoading} = useContext(TransactionContext);

    return (
        <nav className="w-full flex md:justify-center justify-between items-center p-6">
            <div className="md:flex-[0.5] flex-initial justify-center items-center">
            </div>
            <ul className="w-full flex md:justify-center text-white md:flex list-none flex-row justify-between items-center flex-initail">
                <li className='font-sans text-2xl antialiased basis-1/4 pl-12'>Crowdfunding</li>
                <div className='basis-1/2 w-full flex md:justify-center'>
                {routingArray.map((item, index) => (
                    <NavbarItem key={item + index} item={item}/>
                ))}
                </div>
                <li className='basis-1/4 pr-12'>
                    <div className='float-right'>
                    {!currentAccount && (
                    <button
                        type="button"
                        onClick={connectWallet}
                        className="flex flex-row justify-center items-center p-1 rounded-full cursor-pointer"
                    >
                        <p className="font-sans text-2xl antialiased">
                        Connect Wallet
                        </p>
                    </button>
                    )}
                    </div>
                </li>
            </ul>
            <div className="flex relative">
                {toggleMenu
                ? <AiOutlineClose fontSize={28} className="text-white md:hidden cursor-pointer" onClick={() => setToggleMenu(false)} /> 
                : <HiMenuAlt4 fontSize={28} className="text-white md:hidden cursor-pointer" onClick={() => setToggleMenu(true)} />}
                {toggleMenu && (
                    <ul
                    className="z-10 fixed top-0 -right-2 p-3 w-[#70vw] h-screen shadow-2xl md:hidden list-none
                        flex flex-col justify-start items-end rounded-md blue-glassmorphims text-white animate-slide-in 
                    "//TODO: pasek dziala niepoprawnie
                    >
                        <li className="text-xl w-full my-2">
                            <AiOutlineClose onClick={() => setToggleMenu(false)}/>
                        </li>
                        {["Projects", "Wallet", "About us"].map((item, index) => (
                        <NavbarItem key={item + index} item={item} classProps="my-2 text-lg"/>
                        ))}
                    </ul>
                )}
            </div>
        </nav>
    );
}

const DropdownMenu = ({content}) => {
    if (Array.isArray(content) && content.length > 0)
    {
        return (
            <div className='position: absolute bg-transparent  bg-opacity-90 mt-5'>
                <div className='my-4 p-8 shadow-2xl rounded-xl bg-gray-800'>
                    {content.map((item, index) => (
                        <DropdownMenuItem key={item + index}  item={item}/>
                    ))}
                </div>
            </div>
        );
    }
}

const DropdownMenuItem = ({item}) => {
    return (
        <div className='py-3 hover:text-slate-400'>
            <NavLink to={item.toLowerCase().replace(/\s/g, '')}>{item}</NavLink> 
        </div>
    )
}
export default Navbar;