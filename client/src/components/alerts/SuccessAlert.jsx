import { GiConfirmed } from "react-icons/gi";
import { AiOutlineCloseCircle } from "react-icons/ai";

const  SuccessAlert = ({text, close}) => {
    return (
        <div className="fixed bottom-[5%] right-1 max-w-md bg-green-900  rounded-xl z-30">
            <div className="p-3 text-white ">
                <AiOutlineCloseCircle fontSize={21} className="text-white float-right -top-1/2 cursor-pointer" onClick={close}/>
                <GiConfirmed fontSize={21} className="text-white inline-block float-left -top-1/2" />
                <p className="inline-block text-lg pr-1 align-middle">{text}</p>
            </div>
        </div>
    );
}

export default SuccessAlert;