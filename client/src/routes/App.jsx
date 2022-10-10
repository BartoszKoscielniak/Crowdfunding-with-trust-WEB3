import { Navbar, Welcome, Footer, Services, Transactions } from "../components/homepage";
import { TransactionProvider } from '../context/TransactionContext';

const App = () => {
 
  return (
    <TransactionProvider>
    <div className="min-h-screen">
      <div className="gradient-bg-welcome">
        <Navbar/>
        <Welcome/>
      </div>
      <Services/>
      <Transactions/>
      <Footer/>
    </div>
    </TransactionProvider>
  )
}

export default App
