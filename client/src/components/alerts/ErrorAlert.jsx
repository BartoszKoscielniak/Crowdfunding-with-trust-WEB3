import { BiError } from "react-icons/bi";
import { AiOutlineCloseCircle } from "react-icons/ai";

const  ErrorAlert = ({text, close}) => {
    return (
        <div className="fixed bottom-[10%] right-1 max-w-2xl bg-red-900  rounded-xl z-30 drop-shadow-2xl mr-3">
            <div className="p-3 text-white ">
                <AiOutlineCloseCircle fontSize={21} className="text-white float-right -top-1/2 cursor-pointer" onClick={close}/>
                <BiError fontSize={21} className="text-white inline-block float-left -top-1/2" /> 
                <p className="inline-block text-lg pr-1 align-middle"> {text}</p>
            </div>
        </div>
    );
}

export default ErrorAlert;