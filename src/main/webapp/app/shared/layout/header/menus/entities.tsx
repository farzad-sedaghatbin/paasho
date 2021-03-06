import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <DropdownItem tag={Link} to="/entity/event-paasho">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Event Paasho
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/setting-paasho">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Setting Paasho
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/factor-paasho">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Factor Paasho
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/media-paasho">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Media Paasho
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/titles">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Titles
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/category-paasho">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Category Paasho
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/notification-paasho">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Notification Paasho
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/comment-paasho">
      <FontAwesomeIcon icon="asterisk" fixedWidth />
      &nbsp;Comment Paasho
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
