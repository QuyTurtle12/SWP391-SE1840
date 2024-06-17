// import React, { useState,useEffect } from "react";
// import { collection, getDocs } from "firebase/firestore";
// import { db } from "../lib/init-firebase";
// export default function ListProducts() {
//   const [product, setProduct] = useState([]);
// useEffect(() => {
//   getProduct()
 
// }, [])


//   function getProduct() {
//     const productCollectionRef = collection(db, "product");
//     getDocs(productCollectionRef)
//       .then((respone) => {
//         console.log(respone);
//       })
//       .catch((error) => console.log(error.message));
//   }
//   return (
//     <div>
//       <h4>list products</h4>
//     </div>
//   );
// }
