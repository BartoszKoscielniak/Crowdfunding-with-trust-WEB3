import { AccessContext } from '../../context/AccessContext';
import { useContext, useEffect, useState } from 'react';
import { Input } from '..';
import { useRef } from 'react';

const Modal = ({ open, close }) => {
    const { handleChangeRegister, accessError, setAccessError, PerformRegistration, registerData, accessSuccess, setAccessSuccess } = useContext(AccessContext);
    const ref = useRef();
    const {nameInput, surnameInput, emailInput, phoneInput, keyInput, passwordInput} = registerData
    const clickOutside = (e) => {            
        if(ref.current && e.target && !ref.current.contains(e.target)){
            setAccessError(null)
            setAccessSuccess(null)
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
        <div className='w-1/4 bg-indigo-900 shadow-lg shadow-indigo-900/50 fixed top-1/2 left-1/2 p-4 rounded-3xl -translate-x-1/2 -translate-y-1/2 z-10' ref={ref}>
            <p className="font-sans text-2xl antialiased p-1 text-white">
                Register now!
            </p>
            <div className='pb-2'>
                <Input placeholder="Name(Optional)" name="nameInput" type="text" handleChange={handleChangeRegister}/>    
                <Input placeholder="Surname(Optional)" name="surnameInput" type="text"  handleChange={handleChangeRegister}/>    
                <Input placeholder="E-mail" name="emailInput" type="text"  handleChange={handleChangeRegister}/> 
                <Input placeholder="Phone Number" name="phoneInput" type="text"  handleChange={handleChangeRegister}/> 
                <Input placeholder="Private Key" name="keyInput" type="text"  handleChange={handleChangeRegister}/> 
                <Input placeholder="Password" name="passwordInput" type="password"  handleChange={handleChangeRegister}/> 
                {accessError && (
                    <p className='font-sans text-red-600 animate-pulse text-md'>{accessError}</p>
                )}
                {accessSuccess && (
                    <p className='font-sans text-green-600 animate-pulse text-md'>{accessSuccess}</p>
                )}
            </div>
            <div >
                <button 
                onClick={() => {
                    close();
                    setAccessError(null);
                    setAccessSuccess(null)
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
                        if (emailInput === '' || phoneInput === '' || keyInput === '' || passwordInput === '') {
                            setAccessError("All fields must be filled!")
                        }else{
                            setAccessError(null);
                            setAccessSuccess(null);
                            PerformRegistration();
                        }
                    }}
                    className="w-1/3 inline-block border rounded-xl p-2 m-1 float-right hover:text-slate-400"
                >
                    <p className="font-sans text-xl antialiased text-white">
                    Register
                    </p>
                </button>
            </div>
        </div>
    ) : null;
}

export default Modal;