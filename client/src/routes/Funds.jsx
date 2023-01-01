import { Navbar } from "../components/homepage";
import { TransactionProvider } from '../context/TransactionContext';

const Funds = () => {
    
    return (
        <TransactionProvider>
        <Navbar />
        </TransactionProvider>
    );
}

export default Funds