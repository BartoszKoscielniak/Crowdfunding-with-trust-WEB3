import { useRouteError } from "react-router-dom";

const ErrorPage = () => {
    const error = useRouteError();
    console.error(error);
    
    return (
        <div id="error-page">
            <h1>Actuall error page</h1>
            <br></br>
            <i>{error.statusText || error.message}</i>
        </div>
    );
}

export default ErrorPage