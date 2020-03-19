
function initWeb3Provider()
{
     ethereum.send('eth_requestAccounts')
        .catch(err => 
            {
                if (err.code === 4001) 
                {
                  console.log('Connection to MetaMask rejected')
                } 
                else
                {
                    console.error(err)
                }
            })
    ethereum.on('accountsChanged', renderAccount);
    return ethereum
}

function renderAccount(addresses)
{
    if(addresses.length === 0)
    {
        console.log("please connect to MetaMask");
        return;
    }
   alert(addresses[0]);
}

const provider = new ethers.providers.Web3Provider(initWeb3Provider());
const signer = provider.getSigner();


//provider.getBalance(getCurrentAddress()).then(balance => console.log(ethers.utils.formatEther(balance)))
//provider.getNetwork().then(network => alert("network name: " + network.name));



