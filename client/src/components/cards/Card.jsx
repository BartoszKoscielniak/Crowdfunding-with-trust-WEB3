const Card = ({color, title, text}) => {
    return (
    <div className={`${color} rounded-xl px-4 py-2 my-2 drop-shadow-lg antialiased backdrop-blur-lg opacity-90 `}>
        <p className="font-sans font-bold text-3xl antialiased pb-5 inline-block pr-5 text-center text-white">{title}</p>
        <p className="font-sans text-xl pb-10 leading-5 inline-block text-white">{text}</p>
    </div>
    );
}

export default Card;