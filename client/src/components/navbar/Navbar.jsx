import { useState, useContext } from 'react';
import { HiMenuAlt4 } from 'react-icons/hi';
import { AiOutlineClose } from 'react-icons/ai'
import { AccessContext } from '../../context/AccessContext';

import { Link } from 'react-router-dom';
import { LoginModal, RegisterModal } from '..';

const NavbarItem = ({ item, classProps}) => {
    const [isShown, setIsShown] = useState(false);

    return (
        <li className={`z-10 mx-4 cursor-pointer display: flex justify-center font-sans text-xl antialiased   ${classProps}`} onMouseEnter={() => setIsShown(true)} onMouseLeave={() => setIsShown(false)}>
            <Link className='hover:text-slate-400' to={'/' + item[0].toLowerCase().replace(/\s/g, '')}>{item[0]}</Link>   
            {isShown && (
                <DropdownMenu content={item[1]} mainRoute={item[0]}/>
            )}
        </li>
    );
}

const Navbar = ({style}) => {
    const [toggleMenu, setToggleMenu] = useState(false); 
    const { login, authenticated, Logout } = useContext(AccessContext);
    const [isLoginOpen, setModalOpen] = useState(() => {
        if(authenticated) {
            false
        }
    });
    const [isRegistrationOpen, setRegistrationOpen] = useState(false)
    const routingArray = [
        ["Your account", []],
        ["Collections", ['Add collections', 'My collections']],
        ["Polls", ['History']],
        ["About us", []]
    ];

    {authenticated && isLoginOpen && (
        setModalOpen(false)
    )}
    
    return (
        <nav className={`w-full flex md:justify-center justify-between items-center p-6 z-10 ${style}`}>
            <div className="md:flex-[0.5] flex-initial justify-center items-center">
            </div>
            <ul className="w-full flex md:justify-center text-white md:flex list-none flex-row justify-between items-center flex-initail">
                <li className='font-sans text-2xl antialiased animate-pulse animate-anim-moving basis-1/4 pl-12'>
                    {!authenticated ? (
                        <p>Crowdfunding</p>
                    ) : (
                    <div>
                        <p className='font-sans text-2xl antialiased animate-pulse'>Hi, {login.slice(0, 5)}...{login.slice(login.length - 4)}</p>
                    </div>
                    )}
                </li>
                <div className='basis-1/2 w-full flex md:justify-center'>
                {routingArray.map((item, index) => (
                    <NavbarItem key={item + index} item={item}/>
                ))}
                </div>
                <li className='basis-1/4 pr-12'>
                    <div className='float-right'>
                    {!authenticated ? (
                    <button
                        type="button"
                        onClick={() => setModalOpen(true)}
                        className="flex flex-row justify-center items-center p-1 rounded-full cursor-pointer"
                    >
                        <p className="font-sans text-2xl antialiased animate-pulse animate-anim-moving">
                        Log In
                        </p>
                    </button>
                    ) : (
                        <button
                        type="button"
                        onClick={() => Logout()}
                        className="flex flex-row justify-center items-center p-1 rounded-full cursor-pointer"
                    >
                        <p className="font-sans text-2xl antialiased animate-pulse animate-anim-moving">
                        Logout
                        </p>
                    </button>
                    )}
                    <LoginModal open = {isLoginOpen} close = {() => setModalOpen(false)} openRegistration = {() => setRegistrationOpen(true)}/>
                    <RegisterModal open = {isRegistrationOpen} close = {() => setRegistrationOpen(false)}/>
                    </div>
                </li>
            </ul>
            <div className="flex relative">
                {toggleMenu
                ? <AiOutlineClose fontSize={28} className="text-white md:hidden cursor-pointer" onClick={() => setToggleMenu(false)} /> 
                : <HiMenuAlt4 fontSize={28} className="text-white md:hidden cursor-pointer" onClick={() => setToggleMenu(true)} />}
                {toggleMenu && (
                    <ul
                    className="z-10 fixed top-0 right-2 p-3 w-[#70vw] h-screen shadow-2xl md:hidden list-none
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

const DropdownMenu = ({content, mainRoute}) => {
    if (Array.isArray(content) && content.length > 0)
    {
        return (
            <div className='z-10 position: absolute bg-transparent  bg-opacity-90 mt-5'>
                <div className='my-4 p-8 shadow-2xl rounded-xl bg-zinc-800 opacity-96'>
                    {content.map((item, index) => (
                        <DropdownMenuItem key={item + index}  name={item} route={'/' + mainRoute.toLowerCase().replace(/\s/g, '') + '/' + item.toLowerCase().replace(/\s/g, '')}/>
                    ))}
                </div>
            </div>
        );
    }
}

const DropdownMenuItem = ({name, route}) => {
    return (
        <div className='z-10 py-3 hover:text-slate-400 opacity-100'>
            <Link to={route}>{name}</Link> 
        </div>
    )
}

export default Navbar;