import { useRouteError } from "react-router-dom";
import { Background } from "../components";

const ErrorPage = () => {
    const error = useRouteError();
    console.error(error);
    
    return (
        <div>
            <Background />
            <div className="absolute text-xl top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2">
                <i>Message: {error.statusText || error.message}</i>
                <i>Code: {error.status}</i>
            </div>
        </div>
    );
}

export default ErrorPage