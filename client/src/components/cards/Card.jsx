const Card = ({color, title, content}) => {
    return (
    <div className={`${color} rounded-xl px-4 py-2 my-2 drop-shadow-lg antialiased backdrop-blur-lg opacity-90 `}>
        <p className="font-sans font-bold text-3xl antialiased pb-6 inline-block pr-5 text-center text-white">{title}</p>
        {content}
    </div>
    );
}

export default Card;