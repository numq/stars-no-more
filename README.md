<h1 align="center">Stars No More</h1>

<div align="center" style="display: grid; justify-content: center;">

|                                                                  🌟                                                                   |                  Support this project                   |               
|:-------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------:|
|  <img src="https://raw.githubusercontent.com/ErikThiart/cryptocurrency-icons/master/32/bitcoin.png" alt="Bitcoin (BTC)" width="32"/>  | <code>bc1qs6qq0fkqqhp4whwq8u8zc5egprakvqxewr5pmx</code> | 
| <img src="https://raw.githubusercontent.com/ErikThiart/cryptocurrency-icons/master/32/ethereum.png" alt="Ethereum (ETH)" width="32"/> | <code>0x3147bEE3179Df0f6a0852044BFe3C59086072e12</code> |
|  <img src="https://raw.githubusercontent.com/ErikThiart/cryptocurrency-icons/master/32/tether.png" alt="USDT (TRC-20)" width="32"/>   |     <code>TKznmR65yhPt5qmYCML4tNSWFeeUkgYSEV</code>     |

</div>

<br>

<p align="center">An application for getting a summary of statistics and traffic of a user's GitHub repositories</p>

<br>

<p align="center"><img src="./media/preview.png" alt="preview"></p>

> The application was designed using the [Reduce & Conquer](https://github.com/numq/reduce-and-conquer) architectural
> pattern

## Usage

> [!NOTE]
> Data is only available for repositories of type `owner`

- Download latest release and install the application or clone the repository and build the project
- Create a GitHub token with read-only access to public repositories
- Use credentials via in-app input or in a credentials.json file in the following format:
  `{"name":"name","token":"token"}`

## Features

- Dashboard with information about the last 30 pushed repositories
- Information about traffic growth over the last two weeks (last week relative to the penultimate)
- Right-click to open the context menu and refresh the dashboard
- Sorting by criteria: `name`, `stargazers`, `forks`, `clones`, `cloners`, `views`, `visitors`
