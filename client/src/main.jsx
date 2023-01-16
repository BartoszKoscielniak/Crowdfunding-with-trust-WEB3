import * as React from 'react';
import * as ReactDOM from 'react-dom/client';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import { App, ErrorPage, Account, Collections, AddCollection, UsersCollections, Polls, PollsHistory } from './routes';
import { Navigate } from "react-router-dom";
import { AccessProvider } from "./context/AccessContext";

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
    path: "/aboutus",
    element: <Navigate to={{pathname: '/', hash: '#aboutus'}} />,
    errorElement: <ErrorPage />,
  },
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <AccessProvider>
      <RouterProvider router={router}/>
    </AccessProvider>
  </React.StrictMode>
);
