import { Navbar, Welcome, Footer, Services, Transactions, Background } from "../components/homepage";
import { TransactionProvider } from '../context/TransactionContext';
import { Parallax, ParallaxLayer } from '@react-spring/parallax';
import { SiEthereum } from "react-icons/si";

const App = () => {
 
  return (
    <TransactionProvider>
      <Parallax pages={2}>
        <ParallaxLayer
            offset={0}
            speed={0}
            factor={1}
            style={{
              //background: '#805E73',
              backgroundSize: 'cover',
              zIndex: -1,
            }}
            sticky={{ start: 0, end: 1 }}
            
          >
          <Background/>
        </ParallaxLayer>
        <ParallaxLayer
        offset={0}
        style={{
          display: 'block'
        }}
        speed={1}
        >
          <Navbar/>
          <Welcome/>
        </ParallaxLayer>
        <ParallaxLayer
        offset={1}
        style={{
          
        }}
        speed={1}
        >
          <Services/>
          <Transactions/>
          <Footer/>
        </ParallaxLayer>
      </Parallax>
  </TransactionProvider>
  )
}

export default App
