const { Router } = require('express');
const router = Router();

const { check } = require('express-validator');
const { validateInput } = require('../middleware/validate-input');
const { login } = require('../controllers/auth');
const { authenticate } = require('../controllers/auth');

router.post('/login',[
    check('email', 'Email is required').isEmail(),
    check('password', 'Password is required').not().isEmpty(),
    validateInput
],login );

router.post('/authenticate',[
    check('username', 'Username is required').not().isEmail(),
    check('password', 'Password is required').not().isEmpty(),
    // validateInput
], authenticate );

module.exports = router;