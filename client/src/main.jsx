import * as React from 'react';
import * as ReactDOM from 'react-dom/client';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import { App, ErrorPage, Account, Collections, Earn, AddCollection, UsersCollections, Polls, PollsHistory } from './routes';

import "./index.css";

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    errorElement: <ErrorPage />,
  },
  {
    path: "/youraccount",
    element: <Account />,
    errorElement: <ErrorPage />,
  },
  {
    path: "/collections",
    element: <Collections />,
    errorElement: <ErrorPage />
  },
  {
    path: "/polls",
    element: <Polls />,
    errorElement: <ErrorPage />,
  },
  {
    path: "/collections/addcollections",
    element: <AddCollection />,
    errorElement: <ErrorPage />,
  },
  {
    path: "/collections/mycollections",
    element: <UsersCollections />,
    errorElement: <ErrorPage />,
  },
  {
    path: "/polls/history",
    element: <PollsHistory />,
    errorElement: <ErrorPage />,
  },
  {
    path: "/earn",
    element: <Earn />,
    errorElement: <ErrorPage />,
  },
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <RouterProvider router={router}/>
  </React.StrictMode>
);
