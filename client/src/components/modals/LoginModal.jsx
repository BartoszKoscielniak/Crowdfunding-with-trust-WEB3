import { AccessContext } from '../../context/AccessContext';
import { useContext, useEffect, useState } from 'react';
import { Input } from '..';
import { useRef } from 'react';

const Modal = ({ open, close, openRegistration }) => {
    const { PerformLogin, login, handleChange, accessError, setAccessError } = useContext(AccessContext);
    const ref = useRef();

    const clickOutside = (e) => {            
        if(ref.current && e.target && !ref.current.contains(e.target)){
            setAccessError(null)
            close();
        }
    };
      
    const handleScroll = (e) => {
        close();
    };

    useEffect(() => {
      document.addEventListener("click", clickOutside, true);
      //document.addEventListener('scroll', handleScroll, true);
      return () => {
        document.removeEventListener("click", clickOutside, true);
        //document.removeEventListener('scroll', handleScroll, true);
      };
    }, [close])
    
    return open ? (
        <div className='w-1/4 bg-indigo-900 shadow-lg shadow-indigo-900/50 fixed top-1/2 left-1/2 p-4 rounded-3xl -translate-x-1/2 -translate-y-1/2' ref={ref}>
            <p className="font-sans text-2xl antialiased p-1 text-white">
                Log In
            </p>
            <div className='pb-2'>
                <Input id="login" placeholder="Login" name="loginInput" type="text" value={login} handleChange={handleChange}/>    
                <Input id="password" placeholder="Password" name="passwordInput" type="password"  handleChange={handleChange}/>    
                <button 
                className='hover:text-slate-400 text-white'
                onClick={() => {
                        setAccessError(null)
                        openRegistration();
                        close();
                    }
                }
                >
                    Not a user yet? Register now!
                </button>     
                {accessError && (
                    <p className='text-red-600 animate-pulse text-md'>{accessError}</p>
                )}
            </div>
            <div >
                <button 
                onClick={() => {
                    close();
                    setAccessError(null)
                }}
                className="w-1/3 inline-block border rounded-xl p-2 m-1 float-right hover:text-slate-400"
                >
                    <p className="font-sans text-xl antialiased text-white">
                        Close
                    </p>
                </button>
                <button
                    type="button"
                    onClick={() => {
                        PerformLogin();
                        setAccessError(null)
                    }}
                    className="w-1/3 inline-block border rounded-xl p-2 m-1 float-right hover:text-slate-400"
                >
                    <p className="font-sans text-xl antialiased text-white">
                    Log In
                    </p>
                </button>
            </div>
        </div>
    ) : null;
}

export default Modal;