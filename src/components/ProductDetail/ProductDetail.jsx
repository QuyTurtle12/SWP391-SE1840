import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { aget } from '../../net/Axios'
import './ProductDetail.scss'
export default function ProductDetail() {

    let { id } = useParams()
    let [data, setData] = useState({})

    useEffect(() => {
        aget(`/products/${id}`).then(res => {
            setData(res.data)
        })
    }, [])

    return (
        <div className='product-detail'>
            <h1>{data.name}</h1>
            <img src={data.image} alt={data.name} />
            <p>{data.description}</p>
            <p>{data.price}</p>
            <p>{data.stock}</p>
            <p>{data.brand}</p>
            <button>Add to cart</button>

        </div>
    )
}